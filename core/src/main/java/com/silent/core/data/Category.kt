package com.silent.core.data

import com.silent.ilustriscore.core.bean.BaseBean

data class Category(
    override var id: String = "",
    var name: String = "",
    var icon: String = "",
    var color: String = "ffff"
) : BaseBean(id)