package Job1.ua.jrc.web.welcomepage;

import Job1.ua.jrc.web.delete.DeletePage;
import Job1.ua.jrc.web.update.UpdatePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.jrc.bd.entity.User;
import ua.jrc.bd.impldao.H2DAO;

import javax.jws.soap.SOAPBinding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WelcomePage extends WebPage{

    public WelcomePage(){};

    public WelcomePage(PageParameters parameters){

        //Get input data (Login)
        final String loginer = parameters.get("userLoginer").toString();
        //final String loginer = "Login1";


        //Connection to DB
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        final H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

        //Profile Name
        add(new Label("namer",  h2DAO.getUserByLogin(loginer).getName()));
        //Profile Login
        add(new Label("loginer", "(" + loginer + ")"));

        //Update
        Form fupdate = new Form("fupdate");

        Button updateBtn = new Button("update"){
            @Override
            public void onSubmit(){
                super.onSubmit();

                PageParameters parameters = new PageParameters();
                parameters.add("userLoginer", loginer);

                setResponsePage(UpdatePage.class, parameters);
            }
        };

        fupdate.add(updateBtn);
        add(fupdate);

        //Delete
        Form fdelete = new Form("fdelete");

        Button deleteBtn = new Button("delete"){
            @Override
            public void onSubmit(){
                super.onSubmit();

                PageParameters parameters = new PageParameters();
                parameters.add("userLoginer", loginer);

                setResponsePage(DeletePage.class, parameters);
            }
        };
        fdelete.add(deleteBtn);
        add(fdelete);

        //Search
        final Form searchForm = new Form("formSearch");
        User user = new User();

        final TextField name = new TextField("searchText", new PropertyModel(user, "name"));
        name.setOutputMarkupId(true);

        final Label searchUser = new Label("searchU", "");
        searchUser.setOutputMarkupId(true);
        searchUser.setOutputMarkupPlaceholderTag(true);

        AjaxButton searchBtn = new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

                List<User> tempList = h2DAO.getUserListByName(name.getInput());
                StringBuilder stringBuilder = new StringBuilder();
                for (User u: tempList){
                    stringBuilder.append("Login: " + u.getLogin());
                    stringBuilder.append(" Name: " + u.getName());
                    stringBuilder.append(" Password: " + u.getPassword());
                    stringBuilder.append("\n");
                }

                searchUser.setDefaultModelObject(stringBuilder);

                System.out.println(name.getInput());
                target.add(searchUser);
            }
        };
        searchForm.add(searchUser);
        searchForm.add(name);
        searchForm.add(searchBtn);
        add(searchForm);

        //Output users list
        final ListView listView = new ListView("rows", h2DAO.getAllUserList()) {
            @Override
            protected void populateItem(ListItem listItem) {
                User userModel = (User) listItem.getDefaultModelObject();
                listItem.add(new Label("login", userModel.getLogin()));
                listItem.add(new Label("name", userModel.getName()));
                listItem.add(new Label("password", userModel.getPassword()));
            }
        };

        add(listView);
    }

}
