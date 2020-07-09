package io.goooler.demoapp.main.bean

import com.google.gson.annotations.SerializedName
import io.goooler.demoapp.base.model.BaseObjectBoxEntity
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient

@Entity
class RepoListBean(
    @Id(assignable = true) override var id: Long,
    val private: Boolean,
    val fork: Boolean,
    val name: String?,
    val description: String?,
    @Transient val owner: OwnerBean?
) : BaseObjectBoxEntity

class OwnerBean(
    val id: Long,
    val login: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)