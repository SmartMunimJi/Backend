require("dotenv").config();
const express = require("express");
const cors = require("cors");
const logger = require("winston");
const authRoutes = require("./routes/auth.routes");
const userRoutes = require("./routes/user.routes");
const sellerRoutes = require("./routes/seller.routes");
const productRoutes = require("./routes/product.routes");
const claimRoutes = require("./routes/claim.routes");
const errorHandler = require("./middleware/errorHandler.middleware");

const app = express();

app.use(cors({ origin: "*" }));
app.use(express.json());

logger.configure({
  transports: [
    new logger.transports.Console(),
    new logger.transports.File({ filename: "error.log", level: "error" }),
  ],
});

app.use("/smart-munimji", authRoutes);
app.use("/smart-munimji", userRoutes);
app.use("/smart-munimji", sellerRoutes);
app.use("/smart-munimji", productRoutes);
app.use("/smart-munimji", claimRoutes);

app.use(errorHandler);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
