package Job1.ua.jrc.web.login;

import Job1.ua.jrc.web.welcomepage.WelcomePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import ua.jrc.bd.entity.User;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends WebPage {

    public LoginPage(){

        final User user = new User();

        add(new Label("header", "Enter your Login and Password"));

        Form form = new Form("form");

        final TextField login = new TextField("login", new PropertyModel(user, "login"));
        login.setOutputMarkupId(true);

        final TextField password = new TextField("password", new PropertyModel(user, "password"));
        password.setOutputMarkupId(true);

        AjaxButton ajaxButton = new AjaxButton("submit"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form){
                super.onSubmit(target, form);
                System.out.println(login.getInput());
                System.out.println(password.getInput());

                PageParameters parameters = new PageParameters();
                parameters.add("userLoginer", login.getInput());

                setResponsePage(WelcomePage.class, parameters);
            }
        };

        add(form);

        form.add(login);
        form.add(password);
        form.add(ajaxButton);
    }
}
