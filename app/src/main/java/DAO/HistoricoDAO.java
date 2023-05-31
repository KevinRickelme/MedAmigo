package DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Models.Historico;
import Models.Remedio;
import Util.ConnectionFactory;

public class HistoricoDAO {
    private final ConnectionFactory banco;
    private SQLiteDatabase db;

    public HistoricoDAO(Context context){
        banco = new ConnectionFactory(context);
    }

    //Método que insere os dados do usuario no banco de dados
    public long insert(Historico historico){
        db = banco.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("NomeDoRemedio", historico.NomeDoRemedio);
        values.put("DataDaDose", String.valueOf(historico.DataDaDose));

        long result = db.insert(ConnectionFactory.TBL_HISTORICO, null, values);
        db.close();
        return result;
    }

    //Retorna os dados da pessoa cadastrada no banco de dados
    public Historico getHistorico(){
        String[] campos = {"Id","NomeDoRemedio","DataDaDose"};
        db = banco.getReadableDatabase();
        Cursor cursor;
        Historico historico = new Historico();

        try {
            cursor = db.query(ConnectionFactory.TBL_HISTORICO, campos, null, null,null,null,null,"1");
            if(cursor != null && cursor.moveToLast()) {
                historico.Id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
                historico.NomeDoRemedio = cursor.getString(cursor.getColumnIndexOrThrow("NomeDoRemedio"));
                historico.DataDaDose = cursor.getString(cursor.getColumnIndexOrThrow("DataDaDose"));
            }
            db.close();
            return historico;

        }
        catch(Exception ex){
            db.close();
            return historico;
        }
    }
}


