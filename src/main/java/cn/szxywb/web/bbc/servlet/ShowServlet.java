package cn.szxywb.web.bbc.servlet;

import cn.szxywb.web.bbc.bean.db.Transaction;
import cn.szxywb.web.bbc.factory.TransactionFactory;
import cn.szxywb.web.bbc.utils.TextUtil;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/imgShow")
public class ShowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String serialNum = req.getParameter("serialNumber");
        if (Strings.isNullOrEmpty(serialNum))
            return;

        Transaction trans = TransactionFactory.findBySerial(serialNum);
        if (trans == null)
            return;

        List<Transaction> transList = TransactionFactory.findBySellTransId(trans.getId());
        if (transList == null)
            return;

        String basePath = getServletContext().getRealPath("/") + "img/";
        // 进行图片地址筛选
        List<String> paths = transList.stream()
                .filter(transaction -> !Strings.isNullOrEmpty(transaction.getImgSrc()))
                .map(t -> basePath + t.getImgSrc())
                .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            InputStream inStream = new FileInputStream(paths.get(i));
            builder.append(TextUtil.encodeBase64(readInputStream(inStream)));
            if (i < paths.size() - 1)
                builder.append("&&&");
        }

        resp.getWriter().print(builder.toString());
    }

    @SuppressWarnings("Duplicates")
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
