package com.example.photoupload;

import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.jjoe64.graphview.series.DataPoint;

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
    private static final String COL2="Category";
    private static final String COL3="Name";
    private static final String COL4="Date";
    private static final String COL5="Pollinator";
    private static final String COL6="Image";
    private static final String COL7="Latitude";
    private static final String COL8="Longitude";


    private static final String TABLE2_NAME="login_information";
    private static final String TAG2="UserDatabaseHelper";
    private static final String NAME="NAME";
    private static final String ID="ID";
    private static final String EMAIL="EMAIL";
    private static final String PHONENUM="PHONENUMBER";
    private static final String PASSWORD="PASSWORD";
    ArrayList<String> list=new ArrayList<String>();




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
                + COL7 + " TEXT, "
                + COL8 + " TEXT" +")";
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
    public boolean insertData(String category,String name,String date,String pollinator,Bitmap pic,double latitude,double longitude)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,category);
        contentValues.put(COL3,name);
        contentValues.put(COL4,date);
        contentValues.put(COL5,pollinator);
        contentValues.put(COL7,latitude);
        contentValues.put(COL8,longitude);
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
        contentValues.put(COL6,picPath);
        Log.d(TAG, "addData: Adding " +category+" "+ name+"  " + date +" "
                + "  " + pollinator+"  "+latitude+ " "+longitude+" "+ " to " + TABLE_NAME);
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

    public boolean updateData(String id,String category,String name, String date, String pollinator,Bitmap pic)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,category);
        contentValues.put(COL3,name);
        contentValues.put(COL4,date);
        contentValues.put(COL5,pollinator);
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
        contentValues.put(COL6,picPath);
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
        String picPath=picFinder.getString(picFinder.getColumnIndex(COL6));
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

    public ArrayList<String> getAllCategories()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query= "SELECT * FROM " + TABLE_NAME;
        Cursor data=db.rawQuery(query,null);
        list.add(0,"All Categories");//delete this
        if(data.getCount()>0)
        {
            while(data.moveToNext())
            {
                String categoryName=data.getString(data.getColumnIndex("Category"));
                list.add(categoryName);
            }
        }
        return list;
    }

    public ArrayList<Entry> getAllGraphData(ArrayList<String> list1)
    {
        String stringy;
        ArrayList<Entry> dataVals=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        for(int i=1;i<list1.size();i++)//i=0
        {
            stringy=list1.get(i);
            long counter = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                    "Category=?", new String[]{stringy});
            dataVals.add(new Entry(i,(float)counter));
        }
        return dataVals;
    }

    public ArrayList<Entry> getMonthlyData(String singleCategory,String currDate)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] values=currDate.split(" ");
        String dateValues=values[0];
        String[] dv=dateValues.split("/");
        Double month = Double.parseDouble(dv[0]);
        Double day = Double.parseDouble(dv[1]);
        Double year=Double.parseDouble(dv[2]);
        String minDate=month+"/"+day+"/"+(year-1)+"/";
        String maxDate=currDate;
        ArrayList<Entry> dataVals=new ArrayList<>();
        Cursor cursor=db.query(TABLE_NAME,null,"Category=? AND Date" + " BETWEEN ? AND ?",
                new String[]{singleCategory,minDate + " 00:00:00", maxDate + " 23:59:59"},
                null,null,null,null);
       // long numEachCategory = DatabaseUtils.queryNumEntries(db, TABLE_NAME, "Category=? AND Date" + " BETWEEN ? AND ?",'
        //               new String[]{singleCategory,minDate + " 00:00:00", maxDate + " 23:59:59"});
       // for(int i=0;i<numEachCategory;i++)//These are the number of rows in the correct range
        //{
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String datey = cursor.getString(cursor.getColumnIndex("Date"));
                    String[] getDate=datey.split(" ");
                    String dateNotSplit=getDate[0];
                    String[] actual=dateNotSplit.split("/");
                    float graphMonth = Float.parseFloat(actual[0]);
                    long numEachMonth=DatabaseUtils.queryNumEntries(db,TABLE_NAME,"Date=?",
                            new String[]{String.valueOf(graphMonth)});
                    dataVals.add(new Entry(graphMonth , numEachMonth));
                }
            }
        //}
        return dataVals;
    }

}

