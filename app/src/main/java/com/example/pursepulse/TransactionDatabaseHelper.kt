package com.example.pursepulse

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TransactionDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pursepulse.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Transactions"

        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTransaction(transaction: Transaction): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, transaction.date)
            put(COLUMN_AMOUNT, transaction.amount)
            put(COLUMN_CATEGORY, transaction.category)
            put(COLUMN_TYPE, transaction.type)
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun getAllTransactions(): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))

                val transaction = Transaction(id, date, amount, category, type)
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return transactionList
    }

    // Optional: Add retrieval/update/delete methods here later
}
