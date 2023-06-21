import axios from 'axios'
import { getBaseHeader } from '@/utils/request'

export function upload(api, file) {
  var data = new FormData()
  data.append('file', file)
  const config = {
    headers: getBaseHeader()
  }
  return axios.post(api, data, config)
}
