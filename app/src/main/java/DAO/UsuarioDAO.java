package DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Models.Usuario;
import Util.ConnectionFactory;

public class UsuarioDAO {

    private final ConnectionFactory banco;
    private SQLiteDatabase db;

    public UsuarioDAO(Context context){
        banco = new ConnectionFactory(context);
    }

    //Método que insere os dados do usuario no banco de dados
    public long insert(Usuario usuario){
        db = banco.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nome", usuario.Nome);

        long result = db.insert(ConnectionFactory.TBL_USUARIOS, null, values);
        db.close();
        return result;
    }

    //Método que faz a atualização dos dados do usuario no banco de dados
    public long update(Usuario usuario){
        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nome", usuario.Nome);
        String where = "Id=1";

        long result = db.update(ConnectionFactory.TBL_USUARIOS, values, where, null);
        db.close();
        return result;
    }

    //Retorna os dados da pessoa cadastrada no banco de dados
    public Usuario getUsuario(){
        String[] campos = {"Id","Nome"};
        db = banco.getReadableDatabase();
        Cursor cursor;
        Usuario usuario = new Usuario();

        try {
            cursor = db.query(ConnectionFactory.TBL_USUARIOS, campos, null, null,null,null,null,"1");
            if(cursor != null && cursor.moveToLast()) {
                usuario.Id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
                usuario.Nome = cursor.getString(cursor.getColumnIndexOrThrow("Nome"));
            }
            db.close();
            return usuario;

        }
        catch(Exception ex){
            db.close();
            return usuario;
        }
    }

    //verifica se já existe um cadastro
    public boolean hasData(){
        int contagem = 0;
        db = banco.getReadableDatabase();
        String[] CountId = {"Count(Id) as contagem"};
        Cursor cursor;
        cursor = db.query(ConnectionFactory.TBL_USUARIOS, CountId, null,null, null, null, null, "1");
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
