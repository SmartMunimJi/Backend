const Joi = require("joi");

const updateProfileSchema = Joi.object({
  name: Joi.string().min(2).max(100).optional(),
  address: Joi.string().min(5).optional(),
}).min(1);

module.exports = { updateProfileSchema };
