import express from "express";

const app = express();
app.use(express.json());

app.get("/health", (_req, res) => res.json({ status: "ok" }));

app.get("/v1/ping", (_req, res) => res.json({ message: "pong" }));

const port = process.env.PORT || 8080;
app.listen(port, () => {
  // eslint-disable-next-line no-console
  console.log(`listening on :${port}`);
});

