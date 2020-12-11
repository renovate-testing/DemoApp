package io.goooler.demoapp.main.bean

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.goooler.demoapp.common.network.BaseResponse

@JsonClass(generateAdapter = true)
@Entity(tableName = "main_repo_list")
class MainRepoListBean : BaseResponse {

  @PrimaryKey
  var id: Long = 0

  @ColumnInfo
  var private: Boolean = false

  @ColumnInfo
  var fork: Boolean = false

  @ColumnInfo
  var name: String? = null

  @ColumnInfo
  var description: String? = null

  @Embedded
  var owner: OwnerBean? = null

  @JsonClass(generateAdapter = true)
  class OwnerBean {
    @field:Json(name = "avatar_url")
    var avatarUrl: String? = null
  }
}
