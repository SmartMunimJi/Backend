const Joi = require("joi");

const registerCustomerSchema = Joi.object({
  name: Joi.string().min(2).max(100).required(),
  email: Joi.string().email().required(),
  password: Joi.string().min(8).required(),
  phoneNumber: Joi.string()
    .pattern(/^\+\d{10,15}$/)
    .required(),
  address: Joi.string().min(5).required(),
});

const loginSchema = Joi.object({
  email: Joi.string().email().required(),
  password: Joi.string().required(),
});

module.exports = { registerCustomerSchema, loginSchema };
