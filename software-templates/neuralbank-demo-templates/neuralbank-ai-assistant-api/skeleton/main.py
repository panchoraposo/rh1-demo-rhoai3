from fastapi import FastAPI

app = FastAPI(title="Neuralbank AI Assistant API")


@app.get("/health")
def health():
    return {"status": "ok"}

