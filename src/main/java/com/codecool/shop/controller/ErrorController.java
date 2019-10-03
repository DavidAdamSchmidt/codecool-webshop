package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.util.Error;
import com.codecool.shop.util.Util;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/error"})
public class ErrorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        Optional<String> message = Optional.ofNullable(req.getSession().getAttribute("errorMessage"))
                .map(Object::toString);
        Optional<Integer> statusCode = Optional.ofNullable(req.getSession().getAttribute("statusCode"))
                .map(Object::toString)
                .map(Integer::parseInt);

        if (message.isPresent() && statusCode.isPresent()) {
            try {
                String error = statusCode.get() + ": " + message.get();
                context.setVariable("error", error);
                System.out.println(error);

                //TODO: render template here
            } catch (NumberFormatException e) {
                Util.handleError(req, resp, HttpServletResponse.SC_BAD_REQUEST, Error.MALFORMED_STATUS_CODE);
            }
        } else {
            resp.sendRedirect("/");
        }
    }
}
