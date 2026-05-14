package bbdd;

import clases.GlobalContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleLogTable implements Table {
    private Database db;
    private int num_line;
    private int num_battle;
    private String log_entry;

    public static void main(String[] args) {
        String url = "jdbc:mysql://civilizations2526.c92s82e2qeo2.eu-north-1.rds.amazonaws.com/civilizations";
        String username = "admin";
        String pass = "Jefecolorado123";
        BattleLogTable blt = new BattleLogTable(new Database(url, username, pass),1,1,"holi");
        
        // insertion test
        blt.insertRow();

        // modifying test
        blt.setLog_entry("aaaa");
        blt.modifyRow();

        // getRow test
        blt.getRow(2);
    }

    private BattleLogTable() {

    }

    public BattleLogTable(Database db, int num_line, int num_battle, String log_entry) {
		super();
		this.db = db;
		this.num_line = num_line;
		this.num_battle = num_battle;
		this.log_entry = log_entry;
	}

    public BattleLogTable(Database db, int num_battle, String log_entry) {
        this.db = db;
        this.num_battle = GlobalContext.num_battle;
        this.log_entry = log_entry;
    }

    public void insertRow() {
        String insertQuery = "INSERT INTO Battle_log (" +
            "num_battle, log_entry" +
            ") VALUES (?, ?)";
        System.out.println("query for inserting data executed");
        try (PreparedStatement ps = db.getConnection().prepareStatement(insertQuery, new String[] { "num_line" });) {
            // inserting data in ddbb
            System.out.println("PreparedStatement created");
            ps.setInt(1, num_battle);
            ps.setString(2, log_entry);

            int affectedRows = ps.executeUpdate();
            System.out.println("insertion in Battle_log executed");

            // saving the id in the attribute for modifying table later
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        num_line = generatedKeys.getInt(1);
                        System.out.println("new num_line: " + num_line);
                    } else {
                        System.out.println("could not obtain generated num_line");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRow(int num_line) {
        String selectQuery = "SELECT num_battle, log_entry "
            + "FROM Battle_log WHERE num_line = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(selectQuery)) {
            ps.setInt(1, num_line);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    num_battle = rs.getInt("num_battle");
                    log_entry = rs.getString("log_entry");
                    System.out.println("recovered row for num_line=" + num_line);
                    
                    // refresh planet_id in case it has changed (for example id we loaded another planet)
                    if (this.num_line != num_line) {
                        System.out.println("num_line before = " + this.num_line + ", num_line now = " + num_line);
                        this.num_line = num_line;
                    }
                } else {
                    System.out.println("no row with num_line=" + num_line + " in the ddbb");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyRow() {
        String updateQuery = "UPDATE Battle_log SET "
            + "num_battle = ?, log_entry = ? "
            + "WHERE num_line = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(updateQuery)) {
            ps.setInt(1, num_battle);
            ps.setString(2, log_entry);
            ps.setInt(3, num_line);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("success modifying the row with num_line=" + num_line);
            } else {
                System.out.println("row with num_line = " + num_line + " not found. no row has been modified");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNum_line(int num_line) {
		this.num_line = num_line;
	}

	public void setNum_battle(int num_battle) {
		this.num_battle = num_battle;
	}

	public void setLog_entry(String log_entry) {
		this.log_entry = log_entry;
	}
}