use actix_web::{get, web, App, HttpResponse, HttpServer, Responder};
use serde::Serialize;

#[derive(Serialize)]
struct Health {
    status: &'static str,
}

#[get("/health")]
async fn health() -> impl Responder {
    HttpResponse::Ok().json(Health { status: "ok" })
}

#[get("/v1/ping")]
async fn ping() -> impl Responder {
    HttpResponse::Ok().json(web::json!({ "message": "pong" }))
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let port: u16 = std::env::var("PORT")
        .ok()
        .and_then(|p| p.parse().ok())
        .unwrap_or(8080);

    HttpServer::new(|| App::new().service(health).service(ping))
        .bind(("0.0.0.0", port))?
        .run()
        .await
}

