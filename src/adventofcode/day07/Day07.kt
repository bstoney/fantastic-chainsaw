package adventofcode.day07

import adventofcode.AdventOfCodeSolution
import adventofcode.peek
import java.util.Stack

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(7, 95437, 24933642)
    }

    override fun part1(input: List<String>): Int {
        return getDirectories(input)
            .filter { it.getSize() <= 100000 }
            .peek { debug(it) }
            .sumOf { it.getSize() }
    }

    override fun part2(input: List<String>): Int {
        val directorySizes = getDirectories(input)
        val availableSpace = 70000000 - directorySizes.maxOf { it.getSize() }
        debug("Available space: $availableSpace")
        val requiredSpace = 30000000 - availableSpace
        debug("Required space: $requiredSpace")

        return directorySizes
            .asSequence()
            .sortedBy { it.getSize() }
            .filter { it.getSize() >= requiredSpace }
            .peek { debug(it) }
            .map { it.getSize() }
            .first()
    }

    private fun getDirectories(input: List<String>): List<Directory> {
        val iterator = input.iterator()
        val dirs = mutableMapOf<String, Directory>()
        val getDir = { path: String, parent: Directory? ->
            dirs[path] ?: run {
                val dir = Directory(path, parent)
                dirs.putIfAbsent(path, dir)
                dir
            }
        }

        val currentPath = Stack<Directory>()
        val root = getDir("/", null)
        currentPath.push(root)

        while (iterator.hasNext()) {
            with(iterator.next()) {
                val currentDir = currentPath.peek()
                when {
                    equals("$ cd /") -> {
                        currentPath.empty()
                        currentPath.push(root)
                    }

                    equals("$ cd ..") -> {
                        currentPath.pop()
                    }

                    startsWith("$ cd ") -> {
                        currentPath.push(getDir("${currentDir.name}/${this.substring(5)}", currentDir))
                    }

                    equals("$ ls") -> {}
                    startsWith("dir ") -> {
                        currentDir.addDir(getDir("${currentDir.name}/${this.substring(4)}", currentDir))
                    }

                    else -> {
                        val (size, file) = this.split(" ", limit = 2)
                        currentDir.addFile(file, size.toInt())
                    }
                }
            }
        }

        return dirs.values.toList()
    }

    class Directory(
        val name: String,
        private val parent: Directory?,
        private val dirs: ArrayList<Directory> = arrayListOf(),
        private val files: MutableMap<String, Int> = mutableMapOf()
    ) {
        private var size: Int? = null

        fun addDir(dir: Directory) {
            contentChanged()
            dirs.add(dir)
        }

        fun addFile(name: String, size: Int) {
            contentChanged()
            files[name] = size
        }

        fun getSize(): Int {
            return size ?: run {
                val calculatedSize = dirs.sumOf { it.getSize() } + files.values.sum()
                size = calculatedSize
                calculatedSize
            }
        }

        private fun contentChanged() {
            size = null
            parent?.contentChanged()
        }
    }
}
