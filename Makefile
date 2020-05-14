report: report.md
	pandoc $< -o report.pdf -V geometry:margin=3cm
