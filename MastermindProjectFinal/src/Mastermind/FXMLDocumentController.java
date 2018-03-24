/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mastermind;


import javafx.scene.control.Alert.AlertType;
/////////////////////
import java.util.ResourceBundle;
import java.util.Random;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

//red, blue, green, black, white, page, yellow, purple,

public class FXMLDocumentController implements Initializable {

    //Colors are the default 8 colors that will use in the game.
    private final String[] Colors = {"page", "red", "orange", "green", "black", "white",  "yellow", "purple"};
    //PlyerChoiceArr will contains the plyer choice in every step.
    private final String[] plyerChoiceArr = {"", "", "", "", "", "", "", ""};
    //GuessStringArr will contains the random Guess color values.
    private final String[] guessStringArr = {"", "", "", "", "", "", "", ""};
    //PlyerChoiceColorArr will the values in every step to save in the Database.
    private final String[] plyerChoiceColorArr = {"...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "..."};
    //TempString will hold the current color that choosed by user and in the same time the current image file name.
    private String tempString;
    // BallsNumber : the range of balls 6 or 4 default 4.
    private int ballsNumber = 4;
    //ColorsNumber : the colors in the Guess color values  and the colored balls number 6 or 8 default: 8.
    private int colorsNumber = 8;
    //BallsRepeat allow to duplicate colors in Guess color values default Yes.
    int ballsRepeat = 1;
    // Plyer tries counter.
    int tries = 0;
    //StartTime The Timestamp that plyer start the game
    private long startTime;
    //tookTime How much time it take to end the game both if the plyer win or not.
    private long tookTime;
    // conn recive the connection from DBConn class
    Connection conn;
    //tableview to show the data from the database/results table.
    private TableView tableview;

    @FXML
    private AnchorPane ap;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private MenuButton ballsmenu;
    @FXML
    private MenuButton colorsmenu;
    @FXML
    private MenuButton repeatcolormenu;
    @FXML
    private ImageView small1;
    @FXML
    private ImageView small2;
    @FXML
    private ImageView small3;
    @FXML
    private ImageView small4;
    @FXML
    private ImageView small5;
    @FXML
    private ImageView small6;
    @FXML
    private ImageView big0;
    @FXML
    private ImageView big1;
    @FXML
    private ImageView big2;
    @FXML
    private ImageView big3;
    @FXML
    private ImageView big4;
    @FXML
    private ImageView big5;
    @FXML
    private ImageView qm5;
    @FXML
    private ImageView qm6;

    @FXML
    private ImageView purpleBall;

    @FXML
    private ImageView pageBall;

    @FXML
    private Pane pane6;//pane if case 6 balls
    @FXML
    private Pane panetoaddbutton;//pane if case 6 balls
    @FXML
    private ImageView imageViewCurrentColor;
    @FXML
    private Button buttonDefult;
    @FXML
    private Button saveGameButton;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonReStart;
    @FXML
    private Label label;
    @FXML
    private Label labe2;
    @FXML
    private Label triesCounter;
    @FXML
    private Label labeRepeat;
    @FXML
    private Label labelBalls;
    @FXML
    private Label labelcolorsnum;

    @FXML
    private TextField plyerNameText;
    @FXML
    Image colorHole = new Image(getClass().getResource("/Mastermind/color_hole.gif").toString());
    @FXML
    private ObservableList<ObservableList> data;
    @FXML
    TableView resultTable;

    @FXML
    public void buildData() throws ClassNotFoundException, SQLException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String connectionURL = "jdbc:derby://localhost:1527/mydb;create=true;user=myuser;password=mypass";
        DBConn DBConnection = new DBConn();
        conn = DBConnection.DBConnect();

        Statement stmt2 = conn.createStatement();
        ResultSet rs = stmt2.executeQuery("select * from results order by id DESC");
        data = FXCollections.observableArrayList();
        //* TABLE COLUMN ADDED DYNAMICALLY
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableview.getColumns().addAll(col);
        }
        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                row.add(rs.getString(i));
            }
            data.add(row);

        }
        tableview.setItems(data);
        tableview.setLayoutX(9);
        tableview.setLayoutY(560);
        tableview.setMaxHeight(240);
        tableview.setPrefHeight(240);
        tableview.setPrefWidth(1110);
        panetoaddbutton.getChildren().addAll(tableview);
        rs.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    public void getResultsRows() throws SQLException, ClassNotFoundException {
        tableview = new TableView();
        buildData();
    }

    @FXML
    public void deleteRows() throws SQLException, ClassNotFoundException {

        String insertTableSQL = "TRUNCATE TABLE results";
        PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
        preparedStatement.executeUpdate();
        tableview = new TableView();
        buildData();
    }

    @FXML
    public void setResultsRows() throws SQLException, ClassNotFoundException {

        tableview = new TableView();
        buildData();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String insertTableSQL = "INSERT INTO results"
                + "( PLYERNAME, GAMEDATE, GAMEDURATION, TRIES, "
                + "try_1, try_2, try_3, try_4, try_5, try_6, try_7, try_8, try_9, try_10  ) VALUES"
                + "(?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
        preparedStatement.setString(1, plyerNameText.getText());
        preparedStatement.setTimestamp(2, timestamp);
        preparedStatement.setInt(3, (int) tookTime);
        preparedStatement.setInt(4, tries);

        preparedStatement.setString(5, plyerChoiceColorArr[1]);
        preparedStatement.setString(6, plyerChoiceColorArr[2]);
        preparedStatement.setString(7, plyerChoiceColorArr[3]);
        preparedStatement.setString(8, plyerChoiceColorArr[4]);
        preparedStatement.setString(9, plyerChoiceColorArr[5]);
        preparedStatement.setString(10, plyerChoiceColorArr[6]);
        preparedStatement.setString(11, plyerChoiceColorArr[7]);
        preparedStatement.setString(12, plyerChoiceColorArr[8]);
        preparedStatement.setString(13, plyerChoiceColorArr[9]);
        preparedStatement.setString(14, plyerChoiceColorArr[10]);
        // execute insert SQL stetement
        preparedStatement.executeUpdate();

        tableview = new TableView();
        buildData();
        saveGameButton.setDisable(true);
        plyerNameText.setDisable(true);
    }

    @FXML
    public void SetstartTime() {
        startTime = System.currentTimeMillis();
    }

    @FXML
    public void GetTime(long timestamp) {
        long now = System.currentTimeMillis();
        tookTime = (long) ((now - startTime) / 1000.0);
    }

    @FXML
    void handleImageClick(MouseEvent event) {
        tempString = ((ImageView) event.getSource()).getId();
        imageViewCurrentColor.setVisible(true);
        Image image = new Image(getClass().getResource("/Mastermind/" + tempString + ".gif").toString());
        imageViewCurrentColor.setImage(image);
    }

    @FXML
    void putBall(MouseEvent event) {
        String text = ((ImageView) event.getSource()).getId();
        ((ImageView) event.getSource()).setImage(new Image(getClass().getResource("/Mastermind/" + tempString + ".gif").toString()));
        int holeNum = Integer.parseInt(text);
        if (ballsNumber == 4) {
            switch (holeNum) {
                case 0:
                    plyerChoiceArr[0] = tempString;
                    break;
                case 1:
                    plyerChoiceArr[1] = tempString;
                    break;
                case 2:
                    plyerChoiceArr[2] = tempString;
                    break;
                case 3:
                    plyerChoiceArr[3] = tempString;
                    break;
            }
        } else if (ballsNumber == 6) {
            switch (holeNum) {
                case 0:
                    plyerChoiceArr[0] = tempString;
                    break;
                case 1:
                    plyerChoiceArr[1] = tempString;
                    break;
                case 2:
                    plyerChoiceArr[2] = tempString;
                    break;
                case 3:
                    plyerChoiceArr[3] = tempString;
                    break;
                case 4:
                    plyerChoiceArr[4] = tempString;
                    break;
                case 5:
                    plyerChoiceArr[5] = tempString;
                    break;
            }
        }
    }

    @FXML
    void newWindowGame(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Mastermind");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void restartGame(MouseEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            Stage stage = (Stage) ap.getScene().getWindow();
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Mastermind");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @FXML
    void startGame(MouseEvent event) {
        buttonDefult.setVisible(true);
        buttonStart.setDisable(true);
        try {
            repeatcolormenu.setDisable(true);
            ballsmenu.setDisable(true);
            colorsmenu.setDisable(true);
        } catch (Exception e) {
        }

        String guessString = "";
        if (ballsRepeat == 0) {
            Random rand = new Random();
            for (int i = 0; i < ballsNumber; i++) {
                
                int n = rand.nextInt(7) + 1;
                int Cond1NotRepeat = 0;// NotRepeat
                int Cond2NoPageOrPurple = 0;//  NoPageOrPurple
                if ((colorsNumber == 6) && ((Colors[n] == "page") || (Colors[n] == "purple"))) {
                    Cond2NoPageOrPurple = 1;
                    System.out.println(Colors[n]);
                    i--;
                    continue;
                }
                for (int y = 0; y < ballsNumber; y++) {
                    if (guessStringArr[y].equals(Colors[n])) {
                        Cond1NotRepeat = 1;
                        break;
                    }
                }
                if (Cond1NotRepeat == 1) {
                    i--;
                    continue;
                }else if (Cond1NotRepeat == 0) {
                    guessStringArr[i] = Colors[n];
                    guessString += Colors[n] + " / ";
                }  
            }
        } else if (ballsRepeat == 1) {
            for (int i = 0; i < ballsNumber; i++) {
                Random rand = new Random();
                int n = rand.nextInt(7) + 1;

                if ((colorsNumber == 6) && ((Colors[n] == "page") || (Colors[n] == "purple"))) {
                    i--;
                    continue;
                }
                guessStringArr[i] = Colors[n];
                guessString += Colors[n] + " / ";
            }
        }
        System.out.println(guessString);
        SetstartTime();
    }

    public void changeImages() {

        try {
            ImageView newbig0 = new ImageView(big0.getImage());
            newbig0.setLayoutY(big0.getLayoutY());
            newbig0.setLayoutX(219);
            ImageView newbig1 = new ImageView(big1.getImage());
            newbig1.setLayoutY(big1.getLayoutY());
            newbig1.setLayoutX(189);
            ImageView newbig2 = new ImageView(big2.getImage());
            newbig2.setLayoutY(big2.getLayoutY());
            newbig2.setLayoutX(159);
            ImageView newbig3 = new ImageView(big3.getImage());
            newbig3.setLayoutY(big3.getLayoutY());
            newbig3.setLayoutX(129);

            ImageView newsmall1 = new ImageView(small1.getImage());
            newsmall1.setLayoutY(small1.getLayoutY());
            newsmall1.setLayoutX(250);
            newsmall1.setFitWidth(10);
            newsmall1.setFitHeight(10);
            ImageView newsmall2 = new ImageView(small2.getImage());
            newsmall2.setLayoutY(small2.getLayoutY());
            newsmall2.setLayoutX(262);
            newsmall2.setFitWidth(10);
            newsmall2.setFitHeight(10);
            ImageView newsmall3 = new ImageView(small3.getImage());
            newsmall3.setLayoutY(small3.getLayoutY());
            newsmall3.setLayoutX(250);
            newsmall3.setFitWidth(10);
            newsmall3.setFitHeight(10);
            ImageView newsmall4 = new ImageView(small4.getImage());
            newsmall4.setLayoutY(small4.getLayoutY());
            newsmall4.setLayoutX(262);
            newsmall4.setFitWidth(10);
            newsmall4.setFitHeight(10);

            panetoaddbutton.getChildren().addAll(newbig0, newbig1, newbig2, newbig3,
                    newsmall1, newsmall2, newsmall3, newsmall4);

            if (ballsNumber == 6) {
                ImageView newbig4 = new ImageView(big4.getImage());
                newbig4.setLayoutY(big4.getLayoutY());
                newbig4.setLayoutX(33);
                ImageView newbig5 = new ImageView(big5.getImage());
                newbig5.setLayoutY(big5.getLayoutY());
                newbig5.setLayoutX(3);

                ImageView newsmall5 = new ImageView(small5.getImage());
                newsmall5.setLayoutY(small5.getLayoutY());
                newsmall5.setLayoutX(250);
                newsmall5.setFitWidth(10);
                newsmall5.setFitHeight(10);
                ImageView newsmall6 = new ImageView(small6.getImage());
                newsmall6.setLayoutY(small6.getLayoutY());
                newsmall6.setLayoutX(262);
                newsmall6.setFitWidth(10);
                newsmall6.setFitHeight(10);
                pane6.getChildren().addAll(newbig4, newbig5);
                panetoaddbutton.getChildren().addAll(newsmall5, newsmall6);
            }

        } catch (Exception e) {
        }

        small1.setLayoutY(small1.getLayoutY() - 30);
        small2.setLayoutY(small2.getLayoutY() - 30);
        small3.setLayoutY(small3.getLayoutY() - 30);
        small4.setLayoutY(small4.getLayoutY() - 30);
        small5.setLayoutY(small5.getLayoutY() - 30);
        small6.setLayoutY(small6.getLayoutY() - 30);
        small1.setImage(colorHole);
        small2.setImage(colorHole);
        small3.setImage(colorHole);
        small4.setImage(colorHole);
        small5.setImage(colorHole);
        small6.setImage(colorHole);

        big0.setLayoutY(big0.getLayoutY() - 30);
        big1.setLayoutY(big1.getLayoutY() - 30);
        big2.setLayoutY(big2.getLayoutY() - 30);
        big3.setLayoutY(big3.getLayoutY() - 30);
        big4.setLayoutY(big4.getLayoutY() - 30);
        big5.setLayoutY(big5.getLayoutY() - 30);
        big0.setImage(colorHole);
        big1.setImage(colorHole);
        big2.setImage(colorHole);
        big3.setImage(colorHole);
        big4.setImage(colorHole);
        big5.setImage(colorHole);
        buttonDefult.setLayoutY(buttonDefult.getLayoutY() - 30);
    }

    @FXML
    void checkPlyerGuess(MouseEvent event)//guessStringArr
    {
        if (((ballsNumber == 4) && (plyerChoiceArr[0] == "" || plyerChoiceArr[1] == "" || plyerChoiceArr[2] == "" || plyerChoiceArr[3] == ""))
                || ((ballsNumber == 6) && (plyerChoiceArr[0] == "" || plyerChoiceArr[1] == "" || plyerChoiceArr[2] == "" || plyerChoiceArr[3] == "" || plyerChoiceArr[4] == "" || plyerChoiceArr[5] == ""))) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("MasterMind");
            alert.setHeaderText("Complet Your raw");
            alert.setContentText("You need to Complet the ball's raw");
            alert.showAndWait();
        } else// if(1)
        {
            tries++;
            if (tries == 10) {
                buttonDefult.setVisible(false);
            }

            triesCounter.setText("Tries Counter: " + tries);
            int whiteSmallRightCounter = 0;
            int redSmallRightCounter = 0;
            String tempStr = "";

            int[] flag = new int[36];
            for (int x = 0; x < ballsNumber; x++) {
                if (guessStringArr[x].equals(plyerChoiceArr[x])) {
                    flag[x] = 1;
                } else {
                    flag[x] = 0;
                }
                //System.out.println(flag[x]);
            }
            for (int slot = 0; slot < ballsNumber; slot++) {
                if (guessStringArr[slot].equals(plyerChoiceArr[slot])) {
                    redSmallRightCounter++;
                } else {
                    for (int s = 0; s < ballsNumber; s++) {
                        if ((flag[s] == 0) && (guessStringArr[slot].equals(plyerChoiceArr[s]))) {
                            whiteSmallRightCounter++;
                            flag[s] = 1;
                            break;
                        }
                    }
                }
                tempStr += plyerChoiceArr[slot] + " / ";
            }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

            labe2.setText(tempStr);
            plyerChoiceColorArr[tries] = tempStr;
            if (redSmallRightCounter > 0) {

                Image image = new Image(getClass().getResource("/Mastermind/red.gif").toString());
                if (ballsNumber == 4) {
                    switch (redSmallRightCounter) {
                        case 4:
                            small4.setImage(image);
                        case 3:
                            small3.setImage(image);
                        case 2:
                            small2.setImage(image);
                        case 1:
                            small1.setImage(image);
                        //break;
                    }
                } else if (ballsNumber == 6) {
                    switch (redSmallRightCounter) {
                        case 6:
                            small6.setImage(image);
                        case 5:
                            small5.setImage(image);
                        case 4:
                            small4.setImage(image);
                        case 3:
                            small3.setImage(image);
                        case 2:
                            small2.setImage(image);
                        case 1:
                            small1.setImage(image);
                        //break;
                    }
                }
            }

            if (whiteSmallRightCounter > 0) {
                 Image image1 = new Image(getClass().getResource("/Mastermind/white.gif").toString());
                switch (ballsNumber) {
                    case 6: //case 6:
                        switch (redSmallRightCounter) {
                            case 0:
                                switch (whiteSmallRightCounter) {
                                    case 6:
                                        small6.setImage(image1);
                                    case 5:
                                        small5.setImage(image1);
                                    case 4:
                                        small4.setImage(image1);
                                    case 3:
                                        small3.setImage(image1);
                                    case 2:
                                        small2.setImage(image1);
                                    case 1:
                                        small1.setImage(image1);
                                }
                                break;

                            case 1:
                                switch (whiteSmallRightCounter) {
                                    case 5:
                                        small6.setImage(image1);
                                    case 4:
                                        small5.setImage(image1);
                                    case 3:
                                        small4.setImage(image1);
                                    case 2:
                                        small3.setImage(image1);
                                    case 1:
                                        small2.setImage(image1);
                                }
                                break;
                            case 2:
                                switch (whiteSmallRightCounter) {
                                    case 4:
                                        small6.setImage(image1);
                                    case 3:
                                        small5.setImage(image1);
                                    case 2:
                                        small4.setImage(image1);
                                    case 1:
                                        small3.setImage(image1);
                                }
                                break;
                            case 3:
                                switch (whiteSmallRightCounter) {
                                    case 3:
                                        small6.setImage(image1);
                                    case 2:
                                        small5.setImage(image1);
                                    case 1:
                                        small4.setImage(image1);
                                }
                                break;
                            case 4:
                                switch (whiteSmallRightCounter) {
                                    case 2:
                                        small6.setImage(image1);
                                    case 1:
                                        small5.setImage(image1);
                                }
                                break;
                            case 5:
                                if (whiteSmallRightCounter == 1) {
                                    small6.setImage(image1);
                                }
                                break;
                        }
                        break; /// break case 6:
                    /////////////////////////////////////////////////////////////////////////////
                    case 4:
                        switch (redSmallRightCounter) {
                            case 0:
                                switch (whiteSmallRightCounter) {
                                    case 4:
                                        small4.setImage(image1);
                                    case 3:
                                        small3.setImage(image1);
                                    case 2:
                                        small2.setImage(image1);
                                    case 1:
                                        small1.setImage(image1);
                                }
                                break;

                            case 1:
                                switch (whiteSmallRightCounter) {
                                    case 3:
                                        small4.setImage(image1);
                                    case 2:
                                        small3.setImage(image1);
                                    case 1:
                                        small2.setImage(image1);
                                }
                                break;

                            case 2:
                                switch (whiteSmallRightCounter) {
                                    case 2:
                                        small4.setImage(image1);
                                    case 1:
                                        small3.setImage(image1);
                                }
                                break;

                            case 3:
                                if (whiteSmallRightCounter == 1) {
                                    small4.setImage(image1);
                                }
                                break;
                        }
                        break;
                }
            }
            ///////////////////////////////////////
            if (tries < 10) {
                changeImages();
            }

            ///// if plyer guess
            if (redSmallRightCounter == ballsNumber) {
                GetTime(startTime);
                resMessage(true);
                saveGameButton.setDisable(false);
                plyerNameText.setDisable(false);
                small1.setVisible(false);
                small2.setVisible(false);
                small3.setVisible(false);
                small4.setVisible(false);
                small5.setVisible(false);
                small6.setVisible(false);
                big0.setVisible(false);
                big1.setVisible(false);
                big2.setVisible(false);
                big3.setVisible(false);
                big4.setVisible(false);
                big5.setVisible(false);
            } else if ((redSmallRightCounter != ballsNumber) && (tries == 10)) {
                GetTime(startTime);
                resMessage(false);
            }
            plyerChoiceArr[0] = "";
            plyerChoiceArr[1] = "";
            plyerChoiceArr[2] = "";
            plyerChoiceArr[3] = "";
            plyerChoiceArr[4] = "";
            plyerChoiceArr[5] = "";
        }
    }

    @FXML
    void resMessage(boolean res) {
        String Msg;
        buttonDefult.setVisible(false);
        if (res) {
            Msg = "Congratulation!";
        } else {
            Msg = "Sorry! Try aging!";
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("MasterMind");
        alert.setHeaderText(Msg);
        alert.setContentText("It took: " + tries + " Tries" + " and: " + tookTime + " Secends");
        alert.showAndWait();
    }

    @FXML
    void setcolorsNumber(ActionEvent event) {

        colorsNumber = Integer.parseInt(((MenuItem) event.getSource()).getId());
        if (colorsNumber == 6) {
            pageBall.setVisible(false);
            purpleBall.setVisible(false);

        } else {
            pageBall.setVisible(true);
            purpleBall.setVisible(true);
        }
        labelcolorsnum.setText(((MenuItem) event.getSource()).getId());
    }

    @FXML
    void setBallsNumber(ActionEvent event) {
        ballsNumber = Integer.parseInt(((MenuItem) event.getSource()).getId());
        if (ballsNumber == 6) {
            pane6.setVisible(true);
            pane6.setVisible(true);
            small5.setVisible(true);
            small6.setVisible(true);
            qm5.setVisible(true);
            qm6.setVisible(true);
        } else {
            pane6.setVisible(false);
            pane6.setVisible(false);
            small5.setVisible(false);
            small6.setVisible(false);
            qm5.setVisible(false);
            qm6.setVisible(false);
        }
        labelBalls.setText(((MenuItem) event.getSource()).getId());
    }

    @FXML
    void setBallsRepeat(ActionEvent event) {////ballsRepeat
        int ifBallsRepeat = Integer.parseInt(((MenuItem) event.getSource()).getId());
        if (ifBallsRepeat == 1) {
            ballsRepeat = 1;
            labeRepeat.setText("Yes");
        } else if (ifBallsRepeat == 0) {
            ballsRepeat = 0;
            labeRepeat.setText("No");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
