package bbdd;

import clases.Battle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnemyArmyTable implements Table {
    private Database db;
    private int civilization_id;
    private int num_battle;
    private int swordsman_threat;
    private int swordsman_destroyed;
    private int spearman_threat;
    private int spearman_destroyed;
    private int crossbow_threat;
    private int crossbow_destroyed;
    private int cannon_threat;
    private int cannon_destroyed;

    public static void main(String[] args) {
        String url = "jdbc:mysql://civilizations2526.c92s82e2qeo2.eu-north-1.rds.amazonaws.com/civilizations";
        String username = "admin";
        String pass = "Jefecolorado123";
        EnemyArmyTable eat = new EnemyArmyTable(
            new Database(url, username, pass),
            1, 1,
            1, 1,
            1, 1,
            1, 1,
            1, 1
        );

        // insertion test
        eat.insertRow();

        // modifying test
        eat.setSpearman_threat(20);
        eat.modifyRow();

        // getRow test
        eat.getRow(1);
    }

    private EnemyArmyTable() {

    }

    public EnemyArmyTable(Database db,
                          int civilization_id,
                          int num_battle,
                          int swordsman_threat,
                          int swordsman_destroyed,
                          int spearman_threat,
                          int spearman_destroyed,
                          int crossbow_threat,
                          int crossbow_destroyed,
                          int cannon_threat,
                          int cannon_destroyed) {
        this.db = db;
        this.civilization_id = civilization_id;
        this.num_battle = num_battle;
        this.swordsman_threat = swordsman_threat;
        this.swordsman_destroyed = swordsman_destroyed;
        this.spearman_threat = spearman_threat;
        this.spearman_destroyed = spearman_destroyed;
        this.crossbow_threat = crossbow_threat;
        this.crossbow_destroyed = crossbow_destroyed;
        this.cannon_threat = cannon_threat;
        this.cannon_destroyed = cannon_destroyed;
    }

    public EnemyArmyTable(Database db, int num_battle, Battle battle) {
        this.db = db;
        this.num_battle = num_battle;
        this.swordsman_threat = battle.getInitialArmies()[1][0];
        this.spearman_threat = battle.getInitialArmies()[1][1];
        this.crossbow_threat = battle.getInitialArmies()[1][2];
        this.cannon_threat = battle.getInitialArmies()[1][3];
        this.swordsman_destroyed = swordsman_threat - battle.getEnemyArmy()[0].size();
        this.spearman_destroyed = spearman_threat - battle.getEnemyArmy()[1].size();
        this.crossbow_destroyed = crossbow_threat - battle.getEnemyArmy()[2].size();
        this.cannon_destroyed = cannon_threat - battle.getEnemyArmy()[3].size();
    }

    @Override
    public void insertRow() {
        String insertQuery = "INSERT INTO Enemy_army (" +
            "num_battle, swordsman_threat, swordsman_destroyed, " +
            "spearman_threat, spearman_destroyed, " +
            "crossbow_threat, crossbow_destroyed, " +
            "cannon_threat, cannon_destroyed" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println("query for inserting data executed");
        try (PreparedStatement ps = db.getConnection().prepareStatement(insertQuery, new String[] { "civilization_id" })) {
            System.out.println("PreparedStatement created");
            ps.setInt(1, num_battle);
            ps.setInt(2, swordsman_threat);
            ps.setInt(3, swordsman_destroyed);
            ps.setInt(4, spearman_threat);
            ps.setInt(5, spearman_destroyed);
            ps.setInt(6, crossbow_threat);
            ps.setInt(7, crossbow_destroyed);
            ps.setInt(8, cannon_threat);
            ps.setInt(9, cannon_destroyed);

            int affectedRows = ps.executeUpdate();
            System.out.println("insertion in Enemy_army executed");

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        civilization_id = generatedKeys.getInt(1);
                        System.out.println("new civilization_id: " + civilization_id);
                    } else {
                        System.out.println("could not obtain generated civilization_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRow(int civilization_id) {
        String selectQuery = "SELECT num_battle, swordsman_threat, swordsman_destroyed, " +
            "spearman_threat, spearman_destroyed, crossbow_threat, crossbow_destroyed, " +
            "cannon_threat, cannon_destroyed " +
            "FROM Enemy_army WHERE civilization_id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(selectQuery)) {
            ps.setInt(1, civilization_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    num_battle = rs.getInt("num_battle");
                    swordsman_threat = rs.getInt("swordsman_threat");
                    swordsman_destroyed = rs.getInt("swordsman_destroyed");
                    spearman_threat = rs.getInt("spearman_threat");
                    spearman_destroyed = rs.getInt("spearman_destroyed");
                    crossbow_threat = rs.getInt("crossbow_threat");
                    crossbow_destroyed = rs.getInt("crossbow_destroyed");
                    cannon_threat = rs.getInt("cannon_threat");
                    cannon_destroyed = rs.getInt("cannon_destroyed");
                    System.out.println("recovered row for civilization_id=" + civilization_id);
                    
                    if (this.civilization_id != civilization_id) {
                        System.out.println("civilization_id before = " + this.civilization_id + ", civilization_id now = " + civilization_id);
                        this.civilization_id = civilization_id;
                    }
                } else {
                    System.out.println("No row found with civilization_id=" + civilization_id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyRow() {
        String updateQuery = "UPDATE Enemy_army SET " +
            "num_battle = ?, swordsman_threat = ?, swordsman_destroyed = ?, " +
            "spearman_threat = ?, spearman_destroyed = ?, " +
            "crossbow_threat = ?, crossbow_destroyed = ?, " +
            "cannon_threat = ?, cannon_destroyed = ? " +
            "WHERE civilization_id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(updateQuery)) {
            ps.setInt(1, num_battle);
            ps.setInt(2, swordsman_threat);
            ps.setInt(3, swordsman_destroyed);
            ps.setInt(4, spearman_threat);
            ps.setInt(5, spearman_destroyed);
            ps.setInt(6, crossbow_threat);
            ps.setInt(7, crossbow_destroyed);
            ps.setInt(8, cannon_threat);
            ps.setInt(9, cannon_destroyed);
            ps.setInt(10, civilization_id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("success modifying the row with civilization_id=" + civilization_id);
            } else {
                System.out.println("row with civilization_id = " + civilization_id + " not found. no row has been modified");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and setters
    public int getCivilization_id() {
        return civilization_id;
    }

    public void setCivilization_id(int civilization_id) {
        this.civilization_id = civilization_id;
    }

    public void setNum_battle(int num_battle) {
        this.num_battle = num_battle;
    }

    public void setSwordsman_threat(int swordsman_threat) {
        this.swordsman_threat = swordsman_threat;
    }

    public void setSwordsman_destroyed(int swordsman_destroyed) {
        this.swordsman_destroyed = swordsman_destroyed;
    }

    public void setSpearman_threat(int spearman_threat) {
        this.spearman_threat = spearman_threat;
    }

    public void setSpearman_destroyed(int spearman_destroyed) {
        this.spearman_destroyed = spearman_destroyed;
    }

    public void setCrossbow_threat(int crossbow_threat) {
        this.crossbow_threat = crossbow_threat;
    }

    public void setCrossbow_destroyed(int crossbow_destroyed) {
        this.crossbow_destroyed = crossbow_destroyed;
    }

    public void setCannon_threat(int cannon_threat) {
        this.cannon_threat = cannon_threat;
    }

    public void setCannon_destroyed(int cannon_destroyed) {
        this.cannon_destroyed = cannon_destroyed;
    }
}