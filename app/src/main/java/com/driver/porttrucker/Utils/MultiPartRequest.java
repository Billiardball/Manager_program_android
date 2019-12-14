package com.driver.porttrucker.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class MultiPartRequest extends Request<String> {
    public Response.Listener<String> mListener = null;
    public Response.ErrorListener mEListener;
    //
    private final File mFilePart;
    private final String mStringPart;
    private Map<String, String> parameters;

    MultipartEntity entity = new MultipartEntity();

    public MultiPartRequest(String url, Response.ErrorListener eListener,
                            Response.Listener<String> rListener, File file, String stringPart,
                            Map<String, String> param) {

        super(Method.POST, url, eListener);
        mListener = rListener;
        mEListener = eListener;
        mFilePart = file;
        mStringPart = stringPart;
        parameters = param;

        buildMultipartEntity();
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {

            entity.writeTo(new CountingOutputStream(bos, mFilePart.length(),
                    null));

        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        //
        mEListener.onErrorResponse(volleyError);

        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        String resStr = "";
        try {
            resStr = new String(response.data);
        } catch (Exception e) {
        }
        return Response.success(resStr, getCacheEntry());
    }

    private void buildMultipartEntity() {

        entity.addPart(mStringPart,  new FileBody(mFilePart));
        try {
            for (String key : parameters.keySet())
                entity.addPart(
                        key,
                        (org.apache.http.entity.mime.content.ContentBody) new StringBody(URLEncoder.encode(parameters.get(key),
                                HTTP.UTF_8)));
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    public static interface MultipartProgressListener {
        void transferred(long transfered, int progress);
    }

    public static class CountingOutputStream extends FilterOutputStream {
        private final MultipartProgressListener progListener;
        private long transferred;
        private long fileLength;

        public CountingOutputStream(final OutputStream out, long fileLength,
                                    final MultipartProgressListener listener) {
            super(out);
            this.fileLength = fileLength;
            this.progListener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            if (progListener != null) {
                this.transferred += len;
                int prog = (int) (transferred * 100 / fileLength);
                this.progListener.transferred(this.transferred, prog);
            }
        }

        public void write(int b) throws IOException {
            out.write(b);
            if (progListener != null) {
                this.transferred++;
                int prog = (int) (transferred * 100 / fileLength);
                this.progListener.transferred(this.transferred, prog);
            }
        }

    }
}
