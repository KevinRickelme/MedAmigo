package DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Models.Remedio;
import Models.Usuario;
import Util.ConnectionFactory;

public class RemedioDAO {
    private final ConnectionFactory banco;
    private SQLiteDatabase db;

    public RemedioDAO(Context context){
        banco = new ConnectionFactory(context);
    }

    //Método que insere os dados do usuario no banco de dados
    public long insert(Remedio remedio){
        db = banco.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nome", remedio.Nome);
        values.put("Intervalo", remedio.Intervalo);

        long result = db.insert(ConnectionFactory.TBL_REMEDIOS, null, values);
        db.close();
        return result;
    }

    //Método que faz a atualização dos dados do usuario no banco de dados
    public long update(Remedio remedio){
        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nome", remedio.Nome);
        values.put("Intervalo", remedio.Intervalo);
        String where = "Id=1";

        long result = db.update(ConnectionFactory.TBL_REMEDIOS, values, where, null);
        db.close();
        return result;
    }

    //Retorna os dados da pessoa cadastrada no banco de dados
    public Remedio getRemedio(){
        String[] campos = {"Id","Nome","Intervalo"};
        db = banco.getReadableDatabase();
        Cursor cursor;
        Remedio remedio = new Remedio();

        try {
            cursor = db.query(ConnectionFactory.TBL_REMEDIOS, campos, null, null,null,null,null,"1");
            if(cursor != null && cursor.moveToLast()) {
                remedio.Id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
                remedio.Nome = cursor.getString(cursor.getColumnIndexOrThrow("Nome"));
                remedio.Intervalo = cursor.getInt(cursor.getColumnIndexOrThrow("Intevalo"));
            }
            db.close();
            return remedio;

        }
        catch(Exception ex){
            db.close();
            return remedio;
        }
    }

    //verifica se já existe um cadastro
    public boolean hasData(){
        int contagem = 0;
        db = banco.getReadableDatabase();
        String[] CountId = {"Count(Id) as contagem"};
        Cursor cursor;
        cursor = db.query(ConnectionFactory.TBL_REMEDIOS, CountId, null,null, null, null, null, "1");
        if(cursor != null && cursor.moveToLast()) {
            contagem = cursor.getInt(cursor.getColumnIndexOrThrow("contagem"));
        }
        if(contagem > 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }
}
