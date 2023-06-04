package DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.Historico;
import Util.ConnectionFactory;

public class HistoricoDAO {
    private final ConnectionFactory banco;
    private SQLiteDatabase db;

    public HistoricoDAO(Context context){
        banco = new ConnectionFactory(context);
    }

    //Método que insere os dados do usuario no banco de dados
    public long insert(Historico historico){
        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NomeDoRemedio", historico.NomeDoRemedio);
        values.put("DataDaDose", String.valueOf(historico.DataDaDose));
        values.put("Atrasou", String.valueOf(historico.Atrasou));

        long result = db.insert(ConnectionFactory.TBL_HISTORICO, null, values);
        db.close();
        return result;
    }

    //Retorna os dados da pessoa cadastrada no banco de dados
    public List<Historico> getHistorico(){
        String[] campos = {"Id","NomeDoRemedio","DataDaDose", "Atrasou"};
        db = banco.getReadableDatabase();
        Cursor cursor;
        List<Historico> registros = new ArrayList<>();

        try {
            cursor = db.query(ConnectionFactory.TBL_HISTORICO, campos, null, null,null,null,"DataDaDose DESC",null);
            if (cursor != null && cursor.moveToFirst()) {
                do{
                    Historico historico = new Historico();
                    historico.Id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
                    historico.NomeDoRemedio = cursor.getString(cursor.getColumnIndexOrThrow("NomeDoRemedio"));
                    historico.DataDaDose = cursor.getString(cursor.getColumnIndexOrThrow("DataDaDose"));
                    historico.Atrasou = cursor.getString(cursor.getColumnIndexOrThrow("Atrasou"));
                    registros.add(historico);
                }while (cursor.moveToNext());
            }

            db.close();
            return registros;

        }
        catch(Exception ex){
            db.close();
            return registros;
        }
    }
}


