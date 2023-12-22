const express = require('express');
const app = express();
const bodyParser = require('body-parser');

app.use(bodyParser.json());

const userAuthAPI = require('./UserAuth/api');
const uploadAPI = require('./blob/upload');
const imageController = require('./blob/imageController');

app.use('/user', userAuthAPI);
app.use('/upload', uploadAPI);

// Endpoint untuk deteksi katarak
app.post('/detectCataract', async (req, res) => {
  try {
    // Ambil data gambar dari body request
    const imageData = req.body.imageData;

    // Lakukan pemrosesan gambar sesuai kebutuhan model.json
    const result = await imageController.detectCataract(imageData);

    // Tentukan threshold untuk menetapkan apakah gambar memiliki katarak atau tidak
    const threshold = 0.5; // Sesuaikan dengan nilai threshold yang sesuai
    const isCataract = result > threshold;

    res.status(200).json({ message: 'Cataract detection successful', isCataract });
  } catch (error) {
    res.status(500).json({ error: 'Error in cataract detection: ' + error.message });
  }
});

const PORT = process.env.PORT || 3030;
app.listen(PORT, () => {
  console.log(`Awesome! Server is running on port ${PORT}`);
});

module.exports = app;