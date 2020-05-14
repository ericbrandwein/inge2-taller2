entregable: src.zip results report
	zip --junk-paths entregable.zip src/sootOutput/A.jimple report.pdf src.zip

report: report.md
	pandoc $< -o report.pdf -V geometry:margin=3cm

src.zip: src/
	zip --recurse-paths src.zip src/ && \
	zip --delete src.zip "*target/*" src/.gitignore "*.settings" "src/sootOutput/*" "*.DS_Store" "src/.idea/*" "*.class"

results: src/
	cd src/ && ./ejecutar.sh

clean:
	rm entregable.zip src.zip report.pdf