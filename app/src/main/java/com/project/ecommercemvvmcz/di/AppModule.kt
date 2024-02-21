package com.example.firebasewithmvvm.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.project.ecommercemvvmcz.ui.Home.Model.FirebaseCommon
import com.project.ecommercemvvmcz.util.SharedPrefConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPrefConstants.LOCAL_SHARED_PREF,Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

   @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseCommon(firbaseAuth: FirebaseAuth,
                              firestore:FirebaseFirestore)=FirebaseCommon(firestore,firbaseAuth)
    /*@Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) :Db
    {
        return Db.invoke(context)
    }

    @Provides
    @Singleton
    fun getDao(db:Db) :CartDau
    {
        return db.getCartDau()
    }*/




}