package models

case class User(id: Int, username: String, password: String, email: String)

case class UserFormData(username: String, password: String, email: String)

