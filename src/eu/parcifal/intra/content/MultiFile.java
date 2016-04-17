package eu.parcifal.intra.content;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPRequest;

public class MultiFile extends File {

    public MultiFile(String location) {
        super(location);
    }

    @Override
    protected HTTPMessageBody getMessageBody(HTTPRequest request) {
        Pattern pattern = Pattern.compile("[/+]([^+]*)");
        Matcher matcher = pattern.matcher(request.getRequestLine().getRequestURI().getPath());

        byte[] contentBody = new byte[0];
        byte[] newLine = "\r\n\r\n".getBytes();

        while (matcher.find()) {
            byte[] file = super.load(this.location);

            if (contentBody.length == 0) {
                contentBody = file;
            } else {
                byte[] content = new byte[contentBody.length + newLine.length + file.length];

                System.arraycopy(contentBody, 0, content, 0, contentBody.length);
                System.arraycopy(newLine, 0, content, contentBody.length, newLine.length);
                System.arraycopy(file, 0, content, contentBody.length + newLine.length, file.length);

                contentBody = content;
            }
        }

        return new HTTPMessageBody(contentBody);
    }

}
