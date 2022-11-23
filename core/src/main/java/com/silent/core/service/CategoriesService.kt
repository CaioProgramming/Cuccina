package com.silent.core.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.silent.core.data.Category
import com.silent.core.mock.MockData
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult

class CategoriesService : BaseService() {
    override val dataPath = "categories"

    override suspend fun getAllData(): ServiceResult<DataException, ArrayList<BaseBean>> {
        return ServiceResult.Success(ArrayList(MockData.categoriesExamples))
    }

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): BaseBean? {
        return dataSnapshot.toObject(Category::class.java)
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): BaseBean {
        return dataSnapshot.toObject(Category::class.java)
    }
}