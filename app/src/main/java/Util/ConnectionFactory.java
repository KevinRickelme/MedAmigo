package Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConnectionFactory extends SQLiteOpenHelper {

    public static final String NAME =  "MedAmigo.db";
    public static final int VERSION = 1;
    public static final String TBL_USUARIOS = "Usuarios";
    public static final String TBL_REMEDIOS = "Remedios";
    public static final String TBL_HISTORICO = "Historico";

    public ConnectionFactory(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE Usuarios(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nome VARCHAR(250))";

        String sql2 = "CREATE TABLE Remedios(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nome VARCHAR(250)," +
                "Intervalo int)";

        String sql3 = "CREATE TABLE Historico(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NomeDoRemedio VARCHAR(250)," +
                "DataDaDose DateTime)";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql1 = "DROP TABLE IF EXISTS Usuarios";
        String sql2 = "DROP TABLE IF EXISTS Remedios";
        String sql3 = "DROP TABLE IF EXISTS Historico";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);

        onCreate(db);
    }
}
