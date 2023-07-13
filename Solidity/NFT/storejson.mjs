import { NFTStorage } from 'nft.storage'
import { filesFromPath } from 'files-from-path'
import path from 'path'

const API_KEY = '여기에 API KEY를 입력'

async function main() {
  // you'll probably want more sophisticated argument parsing in a real app
  // if (process.argv.length !== 3) {
  //   console.error(`usage: ${process.argv[0]} ${process.argv[1]} <directory-path>`)
  // }
  const directoryPath = "./build/json"
  const files = filesFromPath(directoryPath, {
    pathPrefix: path.resolve(directoryPath), // see the note about pathPrefix below
    hidden: true, // use the default of false if you want to ignore files that start with '.'
  })

  // console.log(files)

  // const one = await files.next()
  // console.log(one)

  const storage = new NFTStorage({ token: API_KEY })

  console.log(`storing file(s) from ${path}`)
  const cid = await storage.storeDirectory(files)
  console.log({ cid })

  const status = await storage.status(cid)
  console.log(status)
}
main()