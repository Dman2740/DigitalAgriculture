package com.example.photoupload;

import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper
{
    /*
    * This is to initialize the table attributes with the column names and table name
     */
    Context context;
    private static final String DATABASE_NAME="PlantInformation.db";
    private static final String TABLE_NAME="plant_information";
    private static final String TAG="DatabaseHelper";
    private static final String COL1="ID";
    private static final String COL2="Name";
    private static final String COL3="Date";
    private static final String COL4="Pollinator";
    private static final String COL5="Image";
    private static final String COL6="Latitude";
    private static final String COL7="Longitude";

    private static final String TABLE2_NAME="login_information";
    private static final String TAG2="UserDatabaseHelper";
    private static final String NAME="NAME";
    private static final String ID="ID";
    private static final String EMAIL="EMAIL";
    private static final String PHONENUM="PHONENUMBER";
    private static final String PASSWORD="PASSWORD";



    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,2);
        this.context=context;
    }

    /*
    * This is to create the table with the various columns and their type is text
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT, "
                + COL3 + " TEXT, "
                + COL4 + " TEXT, "
                + COL5 + " TEXT, "
                + COL6 + " TEXT, "
                + COL7 + " TEXT" +")";
        String CREATE_TABLE2 = "CREATE TABLE " + TABLE2_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + EMAIL + " TEXT, "
                + PHONENUM+ " TEXT, "
                + PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
        Log.d(TAG,"Database was created successfully");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(db);
    }

    /*
    * I am now populating the database with the fields that were entered by the user
    * Also for error checking I am making sure that the data was entered correctly
     */
    public boolean insertData(String name,String date,String pollinator,Bitmap pic,double latitide,double longitude)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,date);
        contentValues.put(COL4,pollinator);
        contentValues.put(COL6,latitide);
        contentValues.put(COL7,longitude);
        String picPath="";
        File internalStorage=context.getDir("PlantPictures",context.MODE_PRIVATE);
        File picFilePath=new File(internalStorage,pollinator+ " .png");
        picPath=picFilePath.toString();
        FileOutputStream fos=null;
        try
        {
            fos = new FileOutputStream(picFilePath);
            pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            Log.i("DATABASE","Problem Updating Picture",e);
            picPath = "";
        }
        contentValues.put(COL5,picPath);
        Log.d(TAG, "addData: Adding " + name+"  " + date+ "  " + pollinator+"  "+latitide+ " "+longitude+" "+ " to " + TABLE_NAME);
        long result=db.insert(TABLE_NAME,null,contentValues);

        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean insertUser(String namey,String email,String pn,String pw)
    {
     SQLiteDatabase db=this.getWritableDatabase();
     ContentValues cv=new ContentValues();
     cv.put(NAME,namey);
     cv.put(EMAIL,email);
     cv.put(PHONENUM,pn);
     cv.put(PASSWORD,pw);
     Log.d(TAG2,"insertUser:Adding "+namey+" "+email+" "+pn+" "+pw+" "+" "+" to "+ TABLE2_NAME);
     long result=db.insert(TABLE2_NAME,null,cv);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkEmail(String email)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor emailFinder=db.query(TABLE2_NAME,null,"EMAIL = ?",new String[]{String.valueOf(email)},null,null,null);
        //Cursor emailFinder=db.rawQuery("select * from login_information where EMAIL = ?",new String[]{email});
        if(emailFinder.getCount()>0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public String checkCredentials(String email)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM login_information where EMAIL = ?",new String[]{email});
        String a,b;
        b="NOT FOUND";
        if(cursor.moveToFirst())
        {
            do {
                a=cursor.getString(2);

                if(a.equals(email))
                {
                    b=cursor.getString(4);
                    break;
                }

            }
            while(cursor.moveToNext());
        }
        return b;
    }

    public Cursor getPlantData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query= "SELECT * FROM " + TABLE_NAME;
        Cursor data=db.rawQuery(query,null);
        return data;
    }
    public Cursor getUserData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query= "SELECT * FROM " + TABLE2_NAME;
        Cursor data=db.rawQuery(query,null);
        return data;
    }

    public boolean updateData(String id,String name, String date, String pollinator,Bitmap pic)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,name);
        contentValues.put(COL3,date);
        contentValues.put(COL4,pollinator);
        String picPath="";
        File internalStorage=context.getDir("PlantPictures",context.MODE_PRIVATE);
        File picFilePath=new File(internalStorage,pollinator+ " .png");
        picPath=picFilePath.toString();
        FileOutputStream fos=null;
        try
        {
            fos = new FileOutputStream(picFilePath);
            pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            Log.i("DATABASE","Problem Updating Picture",e);
            picPath = "";
        }
        contentValues.put(COL5,picPath);
        long result=db.update(TABLE_NAME, contentValues, "ID = ?",new String[]{id});
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public void deleteData(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        db.execSQL(query);
    }

    public Bitmap getPic(String plantId)
    {
        String picturePath = getPicturePath(plantId);
        if (picturePath == null || picturePath.length() == 0)
            return (null);

        Bitmap plantPicture = BitmapFactory.decodeFile(picturePath);
        return (plantPicture);
    }

    private String getPicturePath(String plantId)
    {
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor picFinder=db.query(TABLE_NAME,null,"ID = ?",new String[]{String.valueOf(plantId)},null,null,null);
        picFinder.moveToNext();
        String picPath=picFinder.getString(picFinder.getColumnIndex(COL5));
        return(picPath);
    }


    public void deletePictureFile(String plantID)
    {
        String picPath=getPicturePath(plantID);
        if(picPath!=null && picPath.length()!=0)
        {
            File filePath=new File(picPath);
            filePath.delete();//removing the actual file
        }
        SQLiteDatabase db=this.getWritableDatabase();
        //removing the filepath from the table
        db.delete(TABLE_NAME,"ID = ?",new String[]{String.valueOf(plantID)});

    }

    public long countPollinators()
    {
        long pollinatorCount=0;
        SQLiteDatabase db=getReadableDatabase();
        String sql="SELECT COUNT(*) FROM "+TABLE_NAME;

        Cursor curse=db.rawQuery(sql,null);
        if(curse.getCount()> 0)
        {
            curse.moveToFirst();
            pollinatorCount=curse.getInt(0);
        }
        curse.close();
        return pollinatorCount;
    }


}

