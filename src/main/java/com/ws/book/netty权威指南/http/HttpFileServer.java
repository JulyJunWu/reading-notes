package com.ws.book.netty权威指南.http;

import com.ws.book.netty权威指南.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @author JunWU
 * 使用netty作为文件服务器传输
 */
public class HttpFileServer {

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    @Test
    public void fileServer() {
        Class<? extends ChannelHandler>[] aClass = new Class[]{HttpRequestDecoder.class, HttpObjectAggregator.class, HttpResponseEncoder.class, ChunkedWriteHandler.class, HttpFileServerHandler.class};
        Object[] args = new Object[]{null, new Object[]{65535}, null, null, null};
        Object[] argsType = new Object[]{null, new Class[]{int.class}, null, null, null};

        NettyUtils.startNettyServer(6688, aClass, args, argsType);
    }

    /**
     * 文件下载处理
     */
    public static class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        public static final String FILE_PATH = "/src/main/java/com/ws/book/netty权威指南";

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            boolean keepAlive = isKeepAlive(msg);

            boolean validate = validate(ctx, msg, keepAlive);
            if (!validate) {
                return;
            }

            String uri = msg.uri();
            String path = sanitizeUri(uri);
            if (StringUtil.isNullOrEmpty(path)) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN, keepAlive);
                return;
            }

            File file = new File(path);

            if (!file.exists() || file.isHidden()) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND, keepAlive);
                return;
            }

            if (file.isDirectory()) {
                if (uri.endsWith("/")) {
                    sendListing(ctx, file, keepAlive);
                } else {
                    sendRedirect(ctx, uri + "/", keepAlive);
                }
                return;
            }

            if (!file.isFile()) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN, keepAlive);
                return;
            }

            this.downloadFile(file, ctx, msg);
        }

        private void downloadFile(File file, ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            RandomAccessFile randomAccessFile = null;
            boolean keepAlive = isKeepAlive(msg);
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
            } catch (Exception e) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND, keepAlive);
                randomAccessFile.close();
                return;
            }

            long length = randomAccessFile.length();
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            //设置长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, length);
            //设置类型
            String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file.getPath());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            ctx.write(response);
            ChannelFuture channelFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, length, 8192), ctx.newProgressivePromise());

            channelFuture.addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                    System.out.println("传输完成!");
                }

                @Override
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                    if (total < 0) {
                        System.out.println("进度: " + progress);
                    } else {
                        System.out.println("进度: " + progress + "/" + total);
                    }
                }
            });

            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }

        private boolean validate(ChannelHandlerContext ctx, FullHttpRequest msg, boolean keepAlive) {
            if (!msg.decoderResult().isSuccess()) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, keepAlive);
                return false;
            }

            //必须是GET请求
            if (msg.method() != HttpMethod.GET) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, keepAlive);
                return false;
            }
            return true;
        }

        private String sanitizeUri(String uri) {
            try {
                uri = URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                try {
                    uri = URLDecoder.decode(uri, "ISO-8859-1");
                } catch (UnsupportedEncodingException e1) {
                    throw new Error();
                }
            }

            if ("/".equals(uri)) {
                uri = FILE_PATH;
            } else {
                uri = FILE_PATH + uri;
            }
            uri = uri.replace('/', File.separatorChar);
            return System.getProperty("user.dir") + uri;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
/*            if (ctx.channel().isActive()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }*/
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, boolean keepAlive) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

        ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void sendListing(ChannelHandlerContext ctx, File dir, boolean keepAlive) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

        String dirPath = dir.getPath();
        StringBuilder buf = new StringBuilder();

        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append("目录:");
        buf.append("</title></head><body>\r\n");

        buf.append("<h3>");
        buf.append(dirPath).append(" 目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>链接：<a href=\" ../\")..</a></li>\r\n");
        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
/*            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }*/

            buf.append("<li>链接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }

        buf.append("</ul></body></html>\r\n");

        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri, boolean keepAlive) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static boolean isKeepAlive(HttpMessage httpMessage) {
        try {
            String value = httpMessage.headers().get(HttpHeaderNames.CONNECTION);
            return HttpHeaderValues.KEEP_ALIVE.toString().equals(value);
        } catch (Exception e) {
            return false;
        }
    }


}


