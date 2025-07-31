const Joi = require("joi");

exports.registerProductSchema = Joi.object({
  sellerId: Joi.number().integer().positive().required(),
  orderId: Joi.string().min(1).max(255).required(),
  purchaseDate: Joi.date().iso().less("now").required(),
});
