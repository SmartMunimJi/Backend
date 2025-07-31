const express = require("express");
const dotenv = require("dotenv");
const cors = require("cors");
const authRoutes = require("./routes/auth.routes");
const userRoutes = require("./routes/user.routes");
const sellerRoutes = require("./routes/seller.routes");
const productRoutes = require("./routes/product.routes");
const claimRoutes = require("./routes/claim.routes");
const errorHandler = require("./middleware/errorHandler.middleware");

dotenv.config();
const app = express();

// Enable CORS for Android app
app.use(
  cors({
    origin: "*",
    methods: ["GET", "POST", "PUT"],
    allowedHeaders: ["Content-Type", "Authorization"],
  })
);

app.use(express.json());

// Routes
app.use("/smart-munimji", authRoutes);
app.use("/smart-munimji/users", userRoutes);
app.use("/smart-munimji/sellers", sellerRoutes);
app.use("/smart-munimji/products", productRoutes);
app.use("/smart-munimji/claims", claimRoutes);

// Global Error Handler
app.use(errorHandler);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
