package eu.parcifal.intra.content;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.parcifal.intra.http.HTTPMessageBody;
import eu.parcifal.intra.http.HTTPMessageHeader;
import eu.parcifal.intra.http.HTTPRequest;

public class File extends Content {

    protected String location;

    public File(String location) {
        this.location = location;
    }

    @Override
    protected Collection<HTTPMessageHeader> getMessageHeaders(HTTPRequest request) {
        Collection<HTTPMessageHeader> messageHeaders = new ArrayList<HTTPMessageHeader>();

        Pattern pattern = Pattern.compile("([^.]+)$");
        Matcher matcher = pattern.matcher(String.format(this.location, request.getRequestLine().getRequestURI().getPath()));

        if (matcher.find()) {
            String subtype = matcher.group(1);

            Pattern pattern2 = Pattern.compile("([^\\/, ]+\\/([^;, ]+))(?: *; *q=([^;, ]+))?");
            Matcher matcher2 = pattern2.matcher(new String(request.getMessageHeader("Accept").getFieldValue()));

            String accept = "*/*";
            double acceptQuality = 1;

            while (matcher2.find()) {
                double quality;

                try {
                    quality = Double.valueOf(matcher2.group(3));
                } catch (NullPointerException | IndexOutOfBoundsException exception) {
                    quality = 1;
                }

                if (matcher2.group(2).equals(subtype)) {
                    accept = matcher2.group();
                    break;
                } else if (quality <= acceptQuality) {
                    acceptQuality = quality;
                    accept = matcher2.group(1);
                }
            }

            messageHeaders.add(new HTTPMessageHeader("Content-Type", accept));
        } else {
            messageHeaders.add(new HTTPMessageHeader("Content-Type", "*/*"));
        }

        messageHeaders.add(new HTTPMessageHeader("Access-Control-Allow-Origin", "*"));

        return messageHeaders;
    }

    @Override
    protected HTTPMessageBody getMessageBody(HTTPRequest request) {
        String location = String.format(this.location, request.getRequestLine().getRequestURI().getPath());

        return new HTTPMessageBody(this.load(location));
    }

    public byte[] load(String location) {
        java.io.File file = new java.io.File(location);

        if (file.exists() && file.isFile()) {
            FileInputStream stream = null;

            try {
                byte[] content = new byte[(int) file.length()];

                stream = new FileInputStream(file);

                stream.read(content);

                return content;
            } catch (IOException exception) {
                throw new RuntimeException();
            } finally {
                try {
                    stream.close();
                } catch (IOException exception) {
                    throw new RuntimeException();
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("file at location \"%1$s\" does not exist", location));
        }
    }

}
