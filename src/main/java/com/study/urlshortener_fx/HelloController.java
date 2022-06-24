package com.study.urlshortener_fx;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add_btn;

    @FXML
    private TextField full_link_input;

    @FXML
    private VBox links_panel;

    @FXML
    private Label res_info_label;

    @FXML
    private TextField short_link_input;

    @FXML
    void initialize() throws SQLException, IOException {

        getLinks();
        res_info_label.setText("");

        add_btn.setOnAction(event -> {
            String url_long = full_link_input.getCharacters().toString();
            String url_short = short_link_input.getCharacters().toString();

            if (url_long.isEmpty() || url_short.isEmpty())
                res_info_label.setText("Поле не должно быть пустым");
            else if (!url_long.contains("."))
                res_info_label.setText("Неверный формат ссылки");
            else if (isExists(url_short)) {
                res_info_label.setText("Укажите другое сокращение");
            } else {

                url_long = checkInputLong(url_long);
                addLink(url_long, url_short);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    HelloApplication.setScene("epic-crutch.fxml", stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void addLink(String l, String s) {
        DB db = new DB();
        db.addLink(l, s);
    }

    public boolean isExists (String s) {
        DB db = new DB();
        boolean x = db.isExistsLink(s);
        return x;
    }

    @FXML
    public void getLinks() throws SQLException, IOException {
        DB db = new DB();
        ResultSet resultSet = db.getLinks();

        while (resultSet.next()) {
            Node node = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("link.fxml")));

            Hyperlink link = (Hyperlink) node.lookup("#new_link");
            link.setText(resultSet.getString("url_short"));

            links_panel.getChildren().add(node);
            links_panel.setSpacing(5);
        }
    }

    public String checkInputLong(String l) {
        StringBuilder oneForLong = new StringBuilder(l.replaceAll(" ", ""));

        if (!l.contains("http"))
            l = oneForLong.insert(0, "https://").toString();

        return l;
    }

}
