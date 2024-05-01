package uk.co.tso.tilda;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public interface NoticeDataFilter extends Filter {

    class Default implements NoticeDataFilter {

        private enum RequestType {
            SIMPLE,
            SIMPLE_WITH_ExT,
            DATA,
            OTHER;

            private static final Pattern IS_SIMPLE_NOTICE = Pattern.compile("/notice/[a-zA-Z0-9]+$");
            private static final Pattern IS_SIMPLE_WITH_EXT_NOTICE = Pattern.compile("/notice/[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$");
            private static final Pattern IS_DATA_NOTICE = Pattern.compile("/notice/[a-zA-Z0-9]+/data\\.[a-zA-Z0-9]+$");
            private static boolean isSimpleNotice(String requestURI) {
                var matcher = IS_SIMPLE_NOTICE.matcher(requestURI);
                return matcher.matches();
            }

            private static boolean isSimpleWithExtNotice(String requestURI) {
                var matcher = IS_SIMPLE_WITH_EXT_NOTICE.matcher(requestURI);
                return matcher.matches();
            }

            private static boolean isDataNotice(String requestURI) {
                var matcher = IS_DATA_NOTICE.matcher(requestURI);
                return matcher.matches();
            }

            static RequestType from (String requestURI) {
                if (isSimpleNotice(requestURI))
                    return RequestType.SIMPLE;

                if (isSimpleWithExtNotice(requestURI))
                    return RequestType.SIMPLE_WITH_ExT;

                if (isDataNotice(requestURI))
                    return RequestType.DATA;

                return RequestType.OTHER;
            }
        }

        private final Logger logger = LoggerFactory.getLogger(NoticeDataFilter.class);



        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

            if (servletRequest instanceof HttpServletRequest request) {

                var w = switch(RequestType.from(request.getRequestURI())) {

                    case SIMPLE -> new HttpServletRequestWrapper(request) {
                        @Override
                        public String getRequestURI() {
                            return super.getRequestURI() + ".html";
                        }


                        @Override
                        public String getHeader(String name) {
                            if ("X-TILDA-EFFECTIVE-REQUEST-URI".equals(name)) {
                                StringBuffer url = super.getRequestURL().append("/data.html");
                                String query = super.getQueryString();
                                var hasQuery = StringUtils.hasText(query);
                                if (hasQuery) {
                                    url.append('?').append(query);
                                }
                                var urlString = url.toString();
                                return urlString;
//                                return super.getRequestURI() + "/data.html?" + super.getQueryString();
                            }
                            return super.getHeader(name);
                        }
                        @Override
                        public Enumeration<String> getHeaderNames() {
                            var set = new HashSet<String>();
                            set.add("X-TILDA-EFFECTIVE-REQUEST-URI");

                            var e =  super.getHeaderNames();

                            while(e.hasMoreElements()) {
                                var n = e.nextElement();
                                set.add(n);
                            }

                            return Collections.enumeration(set);
                        }


                    };
                    //We're going to ignore this case, as it doesn't seem to be
                    //use
                    case SIMPLE_WITH_ExT -> new HttpServletRequestWrapper(request) {
                        @Override
                        public Enumeration<String> getHeaderNames() {
                            var set = new HashSet<String>();
                            set.add("X-TILDA-EFFECTIVE-REQUEST-URI");

                            var e =  super.getHeaderNames();

                            while(e.hasMoreElements()) {
                                var n = e.nextElement();
                                set.add(n);
                            }

                            return Collections.enumeration(set);
                        }
                    };
                    case DATA ->  new HttpServletRequestWrapper(request) {
                        @Override
                        public String getRequestURI() {
                            return super.getRequestURI().replace("/data", "");
                        }

                        @Override
                        public String getHeader(String name) {
                            if ("X-TILDA-EFFECTIVE-REQUEST-URI".equals(name)) {
                                StringBuffer url = super.getRequestURL();
                                String query = super.getQueryString();
                                var hasQuery = StringUtils.hasText(query);
                                if (hasQuery) {
                                    url.append('?').append(query);
                                }
                                var urlString = url.toString();
                                return urlString;
                            }
                            return super.getHeader(name);
                        }

                        @Override
                        public Enumeration<String> getHeaderNames() {
                            var set = new HashSet<String>();
                            set.add("X-TILDA-EFFECTIVE-REQUEST-URI");

                            var e =  super.getHeaderNames();

                            while(e.hasMoreElements()) {
                                var n = e.nextElement();
                                set.add(n);
                            }

                            return Collections.enumeration(set);
                        }
                    };
                    case OTHER -> request;
                };

                logger.info("uri: {}", w.getRequestURI());

                filterChain.doFilter(w, servletResponse);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }

        }


    }
}
