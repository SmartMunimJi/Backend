const Joi = require("joi");

const registerProductSchema = Joi.object({
  sellerId: Joi.number().integer().positive().required(),
  orderId: Joi.string().min(1).required(),
  purchaseDate: Joi.date().iso().required(),
});

module.exports = { registerProductSchema };
