.DEFAULT_GOAL := build-run

run-dist:
	make -C app run-dist

clean:
	make -C app clean

build:
	make -C app build

run:
	make -C app run

test:
	make -C app test

lint:
	make -C app lint

report:
	make -C app report

build-run: build run

.PHONY:	build