package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/go-redis/redis"
)

func getAll(rw http.ResponseWriter, r *http.Request) {
	client := redis.NewClient(&redis.Options{
		Addr:     "redis:6379",
		Password: "",
		DB:       0,
	})

	val, err := client.Get("jahans").Result()

	if err != nil {
		fmt.Fprintf(rw, "NO Value!!!")
	}

	fmt.Fprintf(rw, val)
}

func main() {
	h := http.NewServeMux()

	h.HandleFunc("/getAll", getAll)
	err := http.ListenAndServe(":8080", h)
	log.Fatal(err)
	return
}
