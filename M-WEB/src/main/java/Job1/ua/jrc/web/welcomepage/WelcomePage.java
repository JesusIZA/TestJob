package Job1.ua.jrc.web.welcomepage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.jrc.bd.entity.User;
import ua.jrc.bd.impldao.H2DAO;

import java.util.ArrayList;
import java.util.List;

public class WelcomePage extends WebPage {

    public WelcomePage(){};

    public WelcomePage(PageParameters parameters){

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

        add(new Label("loginer", parameters.get("userLoginer")));

        ListView pageableListView = new ListView("rows", h2DAO.getAllUserList()) {
            @Override
            protected void populateItem(ListItem listItem) {
                User userModel = (User)listItem.getDefaultModelObject();
                listItem.add(new Label("login", userModel.getLogin()));
                listItem.add(new Label("name", userModel.getName()));
                listItem.add(new Label("password", userModel.getPassword()));
            }
        };

        add(pageableListView);
    }

}
