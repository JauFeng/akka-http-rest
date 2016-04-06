package com.turing.rest.authentication

final case class LoginForm(mobile: String, password: String)

final case class RegistrationForm(
  mobile: String,
  password: String,
  firstName: String,
  lastName: String,
  email: String,
  gender: String,
  age: Int
)