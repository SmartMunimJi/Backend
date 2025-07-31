const Joi = require("joi");

exports.updateProfileSchema = Joi.object({
  name: Joi.string().min(3).max(50).optional(),
  address: Joi.string().max(255).allow("").optional(),
});
