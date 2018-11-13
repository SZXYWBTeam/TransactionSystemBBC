package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.card.TransactionCard;
import cn.szxywb.web.bbc.utils.RandomValidateCode;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Path("/image")
@SuppressWarnings("Duplicates")
public class ImageService {

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<TransactionCard>> updateLoad(InputStream inStream) {
//        String name = cp.getFileName();

        try {
//            name = new String(name.getBytes("ISO-8859-1"), "UTF-8");

            byte[] data = readInputStream(inStream);
            File file = new File("img/" + "1111.jgp");

            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data);

            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseModel.buildOk();
    }

    public static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        inStream.close();
        return outStream.toByteArray();
    }
}
