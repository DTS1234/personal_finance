// server.js
const jsonServer = require('json-server');
const server = jsonServer.create();
const router = jsonServer.router('db.json'); // Path to your JSON file
const middlewares = jsonServer.defaults();

server.use(middlewares);
server.use(jsonServer.bodyParser);
server.use(jsonServer.rewriter({
  "/:id/summaries": "/summaries",
  "/id/summaries/:id/asset/add": "/assets"
}))

// Custom route for POST /login
server.post('/login', (req, res) => {
  // Mocked response for any login attempt
  console.log("handling login...")
  const response = {
    username: 'testuser',
    id: '123e4567-e89b-12d3-a456-426614174005',
    token: 'mock-jwt-token',
    expiresIn: '3600', // 1 hour expiration time
    userInformation: {
      firstname: 'Test',
      lastname: 'User',
      birthdate: '1990-01-01',
      gender: 'M'
    }
  };

  // Send the mocked response
  res.status(200).jsonp(response);
});

server.get('/123e4567-e89b-12d3-a456-426614174005/summaries/current', (req, res) => {
  // Mocked response for any login attempt
  console.log("handling current...")
  const response = {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "123e4567-e89b-12d3-a456-426614174001",
    "money": 1000,
    "currency": "EUR",
    "state": "DRAFT",
    "date": "2023-10-01",
    "assets": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174002",
        "money": 500,
        "name": "Asset 1",
        "items": [],
        "assetType": "CUSTOM"
      },
      {
        "id": "123e4567-e89b-12d3-a456-426614174003",
        "money": 500,
        "name": "Asset 2",
        "items": [],
        "assetType": "STOCK"
      }
    ]
  };

  // Send the mocked response
  res.status(200).jsonp(response);
});

server.post('/123e4567-e89b-12d3-a456-426614174005/summaries/new', (req, res) => {
  // Mocked response for any login attempt
  console.log("handling summaries...")
  const response = {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "123e4567-e89b-12d3-a456-426614174001",
    "money": 1000,
    "currency": "EUR",
    "state": "DRAFT",
    "date": "2023-10-01",
    "assets": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174002",
        "money": 500,
        "name": "Asset 1",
        "items": [],
        "assetType": "CUSTOM"
      },
      {
        "id": "123e4567-e89b-12d3-a456-426614174003",
        "money": 500,
        "name": "Asset 2",
        "items": [],
        "assetType": "STOCK"
      }
    ]
  };

  // Send the mocked response
  res.status(200).jsonp(response);
});

server.use((req, res, next) => {
  if (req.method === 'GET' && /\/.*\/currency$/.test(req.path)) {
    // Return 'EUR' for all requests matching the pattern
    return res.status(200).send('EUR');
  }

  // Continue to json-server's default router for other routes
  next();
});

// Use default router for all other routes
server.use(router);

server.listen(3000, () => {
  console.log('JSON Server is running on port 3000');
});
