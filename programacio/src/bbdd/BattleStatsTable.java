package bbdd;

import clases.GlobalContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleStatsTable implements Table {
    private Database db;
    private int num_battle;
    private int civilization_id;
    private int resource_wood_acquired;
    private int resource_iron_acquired;
    public static void main(String[] args) {
        String url = "jdbc:mysql://civilizations2526.c92s82e2qeo2.eu-north-1.rds.amazonaws.com/civilizations"; // Local VM Oracle ddbb
        String username = "admin";
        String pass = "Jefecolorado123";
        BattleStatsTable bst = new BattleStatsTable(new Database(url, username, pass),1,1,1,1);
        
        bst.insertRow();

        bst.setResource_deuterion_acquired(100);
        bst.modifyRow();

        // getRow test
        bst.getRow(1);
    }

    private BattleStatsTable() {

    }

    public BattleStatsTable(Database db, int num_battle, int civilization_id, int resource_wood_acquired,
			int resource_iron_acquired) {
		super();
		this.db = db;
		this.num_battle = num_battle;
		this.civilization_id = civilization_id;
		this.resource_wood_acquired = resource_wood_acquired;
		this.resource_iron_acquired = resource_iron_acquired;
	}

    public BattleStatsTable(Database db, int civilization_id, int resource_wood_acquired, 
        int resource_iron_acquired) {
		super();
		this.db = db;
		this.civilization_id = GlobalContext.civilization_id;
		this.resource_wood_acquired = resource_wood_acquired;
		this.resource_iron_acquired = resource_iron_acquired;
	}

    @Override
    public void insertRow() {
        String insertQuery = "INSERT INTO Battle_stats (" +
            "civilization_id, resource_wood_acquired, resource_iron_acquired" +
            ") VALUES (?, ?, ?)";
        System.out.println("query for inserting data executed");
        try (PreparedStatement ps = db.getConnection().prepareStatement(insertQuery, new String[] { "num_battle" });) {
            System.out.println("PreparedStatement created");
            ps.setInt(1, civilization_id);
            ps.setInt(2, resource_wood_acquired);
            ps.setInt(3, resource_iron_acquired);

            int affectedRows = ps.executeUpdate();
            System.out.println("insertion in Battle_stats executed");

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        num_battle = generatedKeys.getInt(1);
                        GlobalContext.num_battle = num_battle;
                        System.out.println("new num_battle: " + num_battle);
                    } else {
                        System.out.println("could not obtain generated num_battle");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRow(int num_battle) {
        String selectQuery = "SELECT civilization_id, resource_wood_acquired, resource_iron_acquired "
            + "FROM Battle_stats WHERE num_battle = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(selectQuery)) {
            ps.setInt(1, num_battle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    civilization_id = rs.getInt("civilization_id");
                    resource_wood_acquired = rs.getInt("resource_wood_acquired");
                    resource_iron_acquired = rs.getInt("resource_iron_acquired");
                    System.out.println("recovered row for num_battle=" + num_battle);
                    
                    if (this.num_battle != num_battle) {
                        System.out.println("num_battle before = " + this.num_battle + ", num_battle now = " + num_battle);
                        this.num_battle = num_battle;
                    }
                } else {
                    System.out.println("no row with num_battle=" + num_battle + " in the ddbb");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyRow() {
        String updateQuery = "UPDATE Battle_stats SET "
            + "civilization_id = ?, resource_wood_acquired = ?, resource_iron_acquired = ? "
            + "WHERE num_battle = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(updateQuery)) {
            ps.setInt(1, civilization_id);
            ps.setInt(2, resource_wood_acquired);
            ps.setInt(3, resource_iron_acquired);
            ps.setInt(4, num_battle);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("success modifying the row with num_battle=" + num_battle);
            } else {
                System.out.println("row with num_battle = " + num_battle + " not found. no row has been modified");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNum_battle(int num_battle) {
		this.num_battle = num_battle;
	}

	public void setCivilization_id(int civilization_id) {
		this.civilization_id = civilization_id;
	}

	public void setResource_metal_acquired(int resource_wood_acquired) {
		this.resource_wood_acquired = resource_wood_acquired;
	}

	public void setResource_deuterion_acquired(int resource_iron_acquired) {
		this.resource_iron_acquired = resource_iron_acquired;
	}

}