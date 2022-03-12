init-env:
	brew bundle
	docker-compose pull
	git config core.hooksPath .githooks

migrate-db:
	script/migrate-db.sh init

docker-up:
	docker-compose up -d
