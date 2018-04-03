from graphviz import Graph

def makeGraph(mat):
	# matriks Jarak terisi, menggunakan GUI untuk menampilkan graf
	dot = Graph()
	for i in range(len(mat)):
		for j,length in mat[i]:
			if length != 0 and j < i:
				dot.edge(str(i), str(j))
	dot.render('graf-awal.dot', view = True)
	return

def makeGraphLoc(loc):
	dot = Graph()
	alpha_track = []
	for point in loc.track:
		alpha_track.append(convert_int_to_alphabeth(point))
	for i in range(len(alpha_track) - 1):
		dot.edge(alpha_track[i], alpha_track[i+1])
	dot.render('graf-akhir.dot', view = True)
	return


class Location:
	def __init__(self, name, adjacent, straight_line, current_cost, track):
		self.name = name # nama lokasi (simpang)
		self.adjacent = adjacent # jarak yang bisa dikunjungi langsung
		self.straight_line = straight_line # straight line
		self.current_cost = current_cost # cost terupdate
		self.track = track # lokasi yang telah dikunjungi

# membaca file dari straight_line.txt dan mengubah menjadi matriks of straight_line
def read_file_straight_line(filename):
	matrix = open(filename, "r")
	straight_line = []
	for line in matrix:
		j = 0
		tuple = []
		for word in line.split():
			tuple.append(int(word))
			j += 1
		straight_line.append(tuple)
	return straight_line

# membaca file cost.txt yang isinya jarak yang bisa dikunjungi langsung dan mengubah menjadi matriks of tuple )lokasi_tujuan, cost)
def read_file_cost(filename):
	matrix = open(filename, "r")
	cost = []
	for line in matrix:
		j = 0
		tuple = []
		for word in line.split():
			tuple.append((j, int(word)))
			j += 1
		cost.append(tuple)
	return cost

# fungsi heuristic f(x) = g(x) + h(x)
def heuristic(location):
	return (location.current_cost + location.straight_line)

# Algoritma A*. Membuat tree dari track yang mungkin hingga ditemukan jalur terpendek secara rekursif
def Astar(lokasi_awal, lokasi_tujuan, cost, straight_line, kunjungi):
	# basis  basis, ketika lokasi_awal = lokasi_tujuan
	if lokasi_awal.name == lokasi_tujuan:
		return lokasi_awal
	else:
		kunjungi.remove(lokasi_awal)
		# membuat lokasi objek dari starting point adjacent
		for start_adjacent in lokasi_awal.adjacent:
			track = []
			adjacent = []

			# set track of the new object from last object
			for location in lokasi_awa	l.track:
				track.append(location)

			# set location adjacent from cost matrix we have made before
			for tuple in cost[start_adjacent]:
				if tuple[1] != 0:
					adjacent.append(tuple[0])

			# build the object
			location = Location(start_adjacent, adjacent, straight_line[start_adjacent][lokasi_tujuan], lokasi_awal.current_cost + cost[lokasi_awal.name][start_adjacent][1], track)
			location.adjacent.remove(lokasi_awal.name) # remove the starting point from adjacent because we don't want to back track (we have been there before)
			location.track.append(location.name) # include this location to the track
			kunjungi.append(location) # include this object to location we are visiting

		best_location = kunjungi[0]
		# find which location has the best heuristic
		for location in kunjungi:
			if heuristic(location) < heuristic(best_location):
				best_location = location
		# Rekursif dengan best location
		return Astar(best_location, lokasi_tujuan, cost, straight_line, kunjungi)

def convert_int_to_alphabeth(x):
	if x == 0:
		return 'A'
	elif x == 1:
		return 'B'
	elif x ==2:
		return 'C'
	elif x == 3:
		return 'D'
	elif x == 4:
		return 'E'
	elif x == 5:
		return 'F'
	elif x == 6:
		return 'G'
	elif x == 7:
		return 'H'
	elif x == 8:
		return 'I'
	elif x == 9:
		return 'J'

def print_track(location):
	alpha_track = []
	for point in location.track:
		alpha_track.append(convert_int_to_alphabeth(point))
	print(alpha_track)

# MAIN PROGRAM

if __name__ == "__main__":
	cost = []
	straight_line = []
	cost = read_file_cost("ITB.txt")
	# str_line = str(input("Silahkan masukkan file jarak antar simpul: "))
	straight_line = read_file_straight_line("Straight_line.txt")

	# print(cost)

	# Tampilkan graf
	makeGraph(cost)

	# User input
	start = int(input("Silahkan masukkan dari simpul mana Anda ingin touring: "))
	lokasi_tujuan = int(input("Silahkan masukkan simpul tempat tujuan Anda: "))

	start_adjacent = []
	for tuple in cost[start]:
		if tuple[1] != 0:
			start_adjacent.append(tuple[0])
	track = [start]

	lokasi_awal = Location(start, start_adjacent, straight_line[start][lokasi_tujuan], 0, track)

	kunjungi = [lokasi_awal]
	final_track = []
	final_track = Astar(lokasi_awal, lokasi_tujuan, cost, straight_line, kunjungi)
	print("\n" + "Jarak terdekat menuju simpul tujuan: ")
	print_track(final_track)
	print("Dengan cost sebesar: ")
	print(final_track.current_cost)

	# Tampilkan Graf
	makeGraphLoc(final_track)
