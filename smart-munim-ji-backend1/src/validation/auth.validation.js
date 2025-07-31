const Joi = require("joi");

exports.registerCustomerSchema = Joi.object({
  name: Joi.string().min(3).max(50).required(),
  email: Joi.string().email().required(),
  password: Joi.string().min(8).max(100).required(),
  phoneNumber: Joi.string()
    .pattern(/^\+\d{10,15}$/)
    .required()
    .messages({
      "string.pattern.base":
        'Phone number must start with "+" and be between 10-15 digits.',
    }),
  address: Joi.string().max(255).allow(""),
});

exports.loginSchema = Joi.object({
  email: Joi.string().email().required(),
  password: Joi.string().required(),
});
