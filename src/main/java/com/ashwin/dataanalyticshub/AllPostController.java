package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllPostController {

    public PieChart pieChart;
    @FXML
    private TableView<SocialMediaPost> postTable;
    @FXML
    private TableColumn<SocialMediaPost, Integer> idCol;
    @FXML
    private TableColumn<SocialMediaPost, String> contentCol;
    @FXML
    private TableColumn<SocialMediaPost, String> authorCol;
    @FXML
    private TableColumn<SocialMediaPost, Integer> likesCol;
    @FXML
    private TableColumn<SocialMediaPost, Integer> sharesCol;
    @FXML
    private TableColumn<SocialMediaPost, LocalDateTime> datesCol;

    private String userName;

    private List<SocialMediaPost> allPosts;
    ObservableList<SocialMediaPost> postsList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        likesCol.setCellValueFactory(new PropertyValueFactory<>("likes"));
        sharesCol.setCellValueFactory(new PropertyValueFactory<>("shares"));
        datesCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        idCol.setPrefWidth(100);
        contentCol.setPrefWidth(250);
        likesCol.setPrefWidth(100);
        sharesCol.setPrefWidth(100);
        authorCol.setPrefWidth(100);
        datesCol.setPrefWidth(200);

        pieChart.setVisible(false);
        pieChart.setManaged(false);

    }

    public void setUserName(String username) {
        this.userName = username;
        boolean isVip = DatabaseHandler.isVip(this.userName);
        if (isVip) {
            pieChart.setVisible(true);
            pieChart.setManaged(true);
        }
        fetchAllPosts(this.userName);
        ObservableList<PieChart.Data> pieChartData = generatePieChartData();
        pieChart.setData(pieChartData);
    }

    private void fetchAllPosts(String username) {
        this.allPosts = DatabaseHandler.getAllPostsByUser(username);
        if (this.allPosts.isEmpty()) {
            pieChart.setVisible(false);
            pieChart.setManaged(false);
        } else {
            pieChart.setManaged(true);
            pieChart.setVisible(true);
        }
        postsList.addAll(allPosts);
        postTable.setItems(postsList);
    }

    public ObservableList<PieChart.Data> generatePieChartData() {

        // Create a HashMap to store category counts
        HashMap<String, Integer> categoryCounts = new HashMap<>();
        categoryCounts.put("0-99", 0);
        categoryCounts.put("100-999", 0);
        categoryCounts.put("1000+", 0);

        // Categorize posts based on shares
        for (SocialMediaPost post : this.allPosts) {
            int shares = post.getShares();

            if (shares >= 1000) {
                categoryCounts.put("1000+", categoryCounts.get("1000+") + 1);
            } else if (shares >= 100) {
                categoryCounts.put("100-999", categoryCounts.get("100-999") + 1);
            } else {
                categoryCounts.put("0-99", categoryCounts.get("0-99") + 1);
            }
        }

        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        return pieChartData;
    }


}
