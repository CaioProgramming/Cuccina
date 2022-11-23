package com.silent.core.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.core.data.Recipe
import com.silent.core.mock.MockData
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult

class RecipesService: BaseService() {
    override val dataPath = "recipes"

    override suspend fun getAllData(): ServiceResult<DataException, ArrayList<BaseBean>> {
        return ServiceResult.Success(ArrayList(MockData.recipesExamples))
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): BaseBean? {
        return dataSnapshot.toObject(Recipe::class.java)?.apply {
            this.id = dataSnapshot.id
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): BaseBean {
        return dataSnapshot.toObject(Recipe::class.java).apply {
            this.id = dataSnapshot.id
        }
    }
}