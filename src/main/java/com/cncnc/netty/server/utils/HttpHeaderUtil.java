package com.cncnc.netty.server.utils;

import com.gome.ad.utils.Hex;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;

/**
 * http头的工具类
 */
public class HttpHeaderUtil {
    private static final String HTTP_X_FORWARDED_FOR = "X-Forwarded-For";

    private static final String COOKIE_GAD_UID_NAME = "asid";

    public static final String COOKIE_DOMAIN = "baidu";

    public static final int COOKIE_MAXAGE = 60 * 60 * 24 * 365 * 10;

    public static String getClientIP(HttpHeaders headers, ChannelHandlerContext ctx) {
        String ips = headers.get(HTTP_X_FORWARDED_FOR);
        if (ips != null) {
            String ip = ips.split(",")[0];
            if (ip != null) {
                return ip;
            }
        } else {
            InetSocketAddress insocket = (InetSocketAddress)ctx.channel().remoteAddress();
            return insocket.getAddress().getHostAddress();
        }
        return null;
    }

    /**
     * set response header info
     *
     * @param response
     * @author luochao
     */
    public static void setCrossDomain(FullHttpResponse response) {
        //cross domain
        response.headers().set("Access-Control-Allow-Origin", "*");
        response.headers().set("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        response.headers().set("Access-Control-Request-Headers", "Content-Type");
        response.headers().set("Access-Control-Allow-Headers", "Content-Type");
        response.headers().set("Access-Control-Allow-Credentials", true);
    }

    /**
     * 从cookie里获取GaxUserId
     *
     * @param headers http的headers
     * @return GaxUserId，没有返回null
     */
    public static String getGaxUserId(HttpHeaders headers) {
        String gaxUID = null;

        String cookieStr = headers.get(HttpHeaderNames.COOKIE);
        if (!StringUtils.isEmpty(cookieStr)) {
            Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieStr);
            if (!cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    if (COOKIE_GAD_UID_NAME.equals(cookie.name())) {
                        gaxUID = cookie.value();
                        break;
                    }
                }
            }
        }
        return gaxUID;
    }

    /**
     * 从cookie里获取CookieName的值
     *
     * @param headers http的headers
     * @param cookieName 需要获取的cookie的name
     * @return str，没有返回null
     */
    public static String getCookieValueByCookieName(HttpHeaders headers, String cookieName) {
        String str = null;

        String cookieStr = headers.get(HttpHeaderNames.COOKIE);
        if (!StringUtils.isEmpty(cookieStr)) {
            Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieStr);
            if (!cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    if (cookieName.equals(cookie.name())) {
                        str = cookie.value();
                        break;
                    }
                }
            }
        }
        return str;
    }

    /**
     * 新建一个GaxUserId
     *
     * @param ua userAgent
     * @return GaxUserId
     */
    public static String createGaxUserId(String ua) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String sourceString = ua + (new Date()).getTime();
        return Hex.encodeHexString(messageDigest.digest(sourceString.getBytes()));
    }

    /**
     * 设置gaxUID的cookie
     *
     * @param httpHeaders http头
     * @param gaxUID      gaxUID
     */
    public static void setGaxUserId(HttpHeaders httpHeaders, String gaxUID) {
        Cookie cookie = new DefaultCookie(COOKIE_GAD_UID_NAME, gaxUID);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setMaxAge(COOKIE_MAXAGE);
        String cookieStr = ServerCookieEncoder.LAX.encode(cookie);
        httpHeaders.add(HttpHeaderNames.SET_COOKIE, cookieStr);
    }
}
